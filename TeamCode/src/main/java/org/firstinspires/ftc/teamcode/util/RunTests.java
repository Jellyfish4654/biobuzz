package org.firstinspires.ftc.teamcode.util;


import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bylazar.telemetry.JoinedTelemetry;
import com.bylazar.telemetry.PanelsTelemetry;
import com.example.jellytest.OnBotTest;
import com.example.jellytest.OnBotTestInfo;
import com.example.jellytest.TestResults;
import com.example.jellytest.TestSet;
import com.example.jellytest.TestTelemetry;
import com.example.jellytest.TestUITelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Utility;

//sinister stuff
import dev.frozenmilk.sinister.Scanner;
import dev.frozenmilk.sinister.targeting.SearchTarget;
import dev.frozenmilk.sinister.targeting.TeamCodeSearch;
import dev.frozenmilk.util.graph.Graph;
import dev.frozenmilk.util.graph.rule.AdjacencyRule;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Utility(name = "JellyTest", description = "Run the JellyTest OnBot test suite")
public class RunTests extends LinearOpMode {

    //customize your test sets here!
    //syntax: new TestSet(String name, boolean isAsync)
    //Tests with the String testSetName set will be assigned to a test set.
    //If this isn't set, don't worry! They'll be apart of a "general" set
    TestSet[] testSets = {

    };



    enum State{
        MENU,
        SELECTING_TEST_SET,
        RUNNING_TEST,
        TEST_RESULT_SUMMARY,
        TO_QUIT
    }

    List<OnBotTest> onBotTests = new ArrayList<>();
    TestSet generalTests = new TestSet("General Test Set",false);

    TestUITelemetry uiTelemetry;
    State state = State.MENU;
    int menuSelected = 0;
    TestSet testSetSelected = null;
    boolean stateChanged = false;

    boolean runAllTests = false;
    int allTestSetsIndex = 0;

    TestResults results = new TestResults();
    Thread[] currentThreads;
    int currentTestIndex = 0;
    boolean testRunning = false;




    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new JoinedTelemetry(telemetry, PanelsTelemetry.INSTANCE.getFtcTelemetry());
        uiTelemetry = new TestUITelemetry(telemetry);
        waitForStart();
        getAnnotatedClasses();
        while(opModeIsActive()){
            //FSM
            if(state==State.MENU){
                runMenu(
                        String.format("JellyTestly v0.01\n%s tests loaded -- %s test sets loaded", onBotTests.size(), testSets.length + 1),
                        new String[]{"Run all tests", "Select test to run"},
                        new String[]{"Dpad Up","Dpad Down","X/A","Triangle/Y"},
                        new String[]{"Up","Down","Select","Quit"}
                );
                if(gamepad1.crossWasPressed()){
                    if(menuSelected==1){
                        state = State.SELECTING_TEST_SET;
                        stateChanged = true;
                    }else{
                        state = State.RUNNING_TEST;
                        runAllTests = true;
                        testSetSelected = testSets.length > 0? testSets[0]:generalTests;
                        stateChanged = true;
                    }
                }

            }
            else if(state==State.SELECTING_TEST_SET){
                if(stateChanged){
                    menuSelected = 0;
                    stateChanged = false;
                }
                String[] choices;
                if(testSetSelected == null){
                    choices = new String[generalTests.getSize()+testSets.length];
                    for(int i=0;i<testSets.length;i++){
                        choices[i] = "*"+testSets[i].name;
                    }
                    for(int i=0;i<generalTests.getSize();i++){
                        choices[i+testSets.length] = onBotTestName(generalTests.getTest(i));
                    }

                    //handle btn presses
                    if(gamepad1.squareWasPressed()){
                        //go back
                        state = State.MENU;
                        menuSelected = 0;
                    }else if(gamepad1.circleWasPressed() && menuSelected<testSets.length){
                        //open set if a set is selected
                        testSetSelected = getTestSetSelectedByIndex(menuSelected);
                        menuSelected = 0;
                    }else if(gamepad1.crossWasPressed()){
                        //run set/test
                        testSetSelected = getTestSetSelectedByIndex(menuSelected);
                        menuSelected = 0;
                        state = State.RUNNING_TEST;
                        stateChanged = true;
                    }
                }else{
                    choices = new String[testSetSelected.getSize()];
                    for(int i=0;i<testSetSelected.getSize();i++){
                        choices[i] = onBotTestName(testSetSelected.getTest(i));
                    }
                    //handle btn presses
                    if(gamepad1.squareWasPressed()){
                        //go back
                        menuSelected = 0;
                        testSetSelected = null;
                    }else if(gamepad1.crossWasPressed()){
                        //run set/test
                        OnBotTest desiredTest = testSetSelected.getTest(menuSelected);
                        testSetSelected = new TestSet(onBotTestName(testSetSelected.getTest(menuSelected)),false);
                        testSetSelected.addTest(desiredTest);
                        menuSelected = 0;
                        state = State.RUNNING_TEST;
                        stateChanged = true;
                    }
                }

                runMenu(
                        "JellyTest v0.01\nSelect test--tests marked with * are test sets\nTest sets are groups of tests",
                        choices,
                        new String[]{"Dpad Down","Dpad Up","Circle/B","X/A","Triangle/Y","Square/X"},
                        new String[]{"Down","Up","Open (Set)","Run Set/Test","Quit","Back"}

                );

            }
            else if(state==State.RUNNING_TEST){
                uiTelemetry.clearAll();

                if(stateChanged){
                    stateChanged=false;

                    menuSelected = 0;
                    currentTestIndex = 0;
                    currentThreads = new Thread[testSetSelected.getSize()];

                    //turn tests into threads
                    for(int i=0;i<testSetSelected.getSize();i++){
                        OnBotTest test = testSetSelected.getTest(i);
                        currentThreads[i] = new Thread(new Runnable() {
                            final OnBotTest testToRun = test;
                            @Override
                            public void run() {
                                int repeats = testToRun.getClass().getAnnotation(OnBotTestInfo.class).repeats();
                                for(int i=0;i<repeats;i++){
                                    testToRun.preRun(i);
                                    testToRun.telemetry.repeatIndex += 1;
                                    testToRun.telemetry.setProgress(0);
                                    testToRun.run();
                                    testToRun.telemetry.setProgress(100);
                                    results.addResult(testSetSelected.name,onBotTestName(testToRun),testToRun.passed);
                                    testToRun.postRun(i);
                                    testToRun.isRunning = true;
                                    testToRun.done = false;
                                }
                                testToRun.isRunning = false;
                                testToRun.done = true;
                                testRunning = false;
                            }
                        },"TestThread"+onBotTestName(test));
                        if(testSetSelected.isAsync){
                            currentThreads[i].start();
                        }
                    }
                }

                //collect telemetry for the tests
                uiTelemetry.addHeaderLine("Set "+testSetSelected.name);
                uiTelemetry.addHeaderLine(String.format(
                        "|%s| <%s/%s> done - %s pass - %s fail",
                        progressBarGenerator(testSetSelected.getSize(), testSetSelected.getAmountDone(), 10), //todo, change length?
                        testSetSelected.getAmountDone(),
                        testSetSelected.getSize(),
                        testSetSelected.getAmountPassed(),
                        testSetSelected.getAmountFailed()
                ));

                //collect individual test telemetry
                for(int i=0; i<testSetSelected.getSize();i++){
                    uiTelemetry.addBodyLine(String.format(
                            (i==menuSelected?"[[":"")+"Test %s <%s/%s> - |%s| - %s"+(i==menuSelected?"]]":""),
                            onBotTestName(testSetSelected.getTest(i)),
                            testSetSelected.getTest(i).telemetry.repeatIndex,
                            testSetSelected.getTest(i).getClass().getAnnotation(OnBotTestInfo.class).repeats(),
                            progressBarGenerator(100,testSetSelected.getTest(i).telemetry.getProgress(),6),
                            results.getTestResultString(testSetSelected.name, onBotTestName(testSetSelected.getTest(i)))
                    ));

                    //test thread can write to this while I'm reading
                    synchronized (testSetSelected.getTest(i).telemetry.lines){
                        for(String line : testSetSelected.getTest(i).telemetry.lines){
                            uiTelemetry.addBodyLine(line);
                        }
                    }


                    uiTelemetry.addBodyLine();
                }


                if(testSetSelected.getTest(menuSelected).waitingForManualApproval){
                    uiTelemetry.setFooterControls(
                            new String[]{"Dpad Up","Dpad Down","X/A","Triangle/Y"},
                            new String[]{"Up","Down","Pass Test","Fail Test"}
                    );
                    if(gamepad1.crossWasPressed()){
                        testSetSelected.getTest(menuSelected).passTest();
                    }else if(gamepad1.triangleWasPressed()){
                        testSetSelected.getTest(menuSelected).failTest();
                    }
                }else{
                    uiTelemetry.setFooterControls(
                            new String[]{"Dpad Up","Dpad Down"},
                            new String[]{"Up","Down"}
                    );
                }

                //along with actually moving the cursor, it clears any buttonpresses
                //this is so if i am on automatic test 0 and I click the cross
                //and i switch to manual test 1, the cross doesnt count
                if(gamepad1.dpadDownWasPressed()){
                    menuSelected = menuSelected == testSetSelected.getSize()-1?0:menuSelected+1;
                    gamepad1.crossWasPressed();
                    gamepad1.triangleWasPressed();
                }else if(gamepad1.dpadUpWasPressed()){
                    menuSelected = menuSelected == 0?testSetSelected.getSize()-1:menuSelected-1;
                    gamepad1.crossWasPressed();
                    gamepad1.triangleWasPressed();
                }

                //all tests done logic for nonasync testsets
                if(!testSetSelected.isAsync && !testRunning){
                    if(currentTestIndex==currentThreads.length){
                        if(runAllTests){
                            stateChanged = true;
                            allTestSetsIndex++;
                            if(allTestSetsIndex==testSets.length){
                                testSetSelected = generalTests;
                            }else if(allTestSetsIndex>testSets.length){
                                state = State.TEST_RESULT_SUMMARY;
                            }else{
                                testSetSelected = testSets[allTestSetsIndex];
                            }
                        }else{
                            stateChanged = true;
                            state = State.TEST_RESULT_SUMMARY;
                        }
                    }else{
                        currentThreads[currentTestIndex].start();
                        testRunning = true;
                        currentTestIndex++;
                    }
                }

                //all tests done logic for async testsets
                if(testSetSelected.isAsync){
                    if(testSetSelected.getAmountDone() == testSetSelected.getSize()){
                        if(runAllTests){
                            stateChanged = true;
                            allTestSetsIndex++;

                            if(allTestSetsIndex==testSets.length){
                                testSetSelected = generalTests;
                            }else if(allTestSetsIndex>testSets.length){
                                state = State.TEST_RESULT_SUMMARY;
                            }else{
                                testSetSelected = testSets[allTestSetsIndex];
                            }
                        }else{
                            stateChanged = true;
                            state = State.TEST_RESULT_SUMMARY;
                        }
                    }
                }


            }
            else if(state==State.TEST_RESULT_SUMMARY){
                uiTelemetry.setFooter("");
                uiTelemetry.setHeader("All done! Results:");
                uiTelemetry.setBody(results.toString());
            }
            else if(state==State.TO_QUIT){
                return;
            }
            uiTelemetry.updateTelemetry();
        }

    }
    /**Utility function for making string progress bars*/
    String progressBarGenerator(double total, double value, int length){
        double amtToFill = (value/total)*length;
        StringBuilder bar = new StringBuilder();
        for(int i=0;i<length;i++){
            if(i+1<=amtToFill){
                bar.append('*');
            }else{
                bar.append('-');
            }
        }
        return bar.toString();
    }
    /**This method either gets the name of the test through it's annotation (if set) or class name*/
    String onBotTestName(OnBotTest test){
        String name = test.getClass().getAnnotation(OnBotTestInfo.class).testName();
        if(name.isEmpty()){
            return test.getClass().getSimpleName();
        }else{
            return name;
        }
    }

    /**Gets either a TestSet or makes a TestSet of one test depending which is selected in the *combined* list */
    TestSet getTestSetSelectedByIndex(int index){
        if(index>=testSets.length){
            OnBotTest test = generalTests.getTest(index-testSets.length);
            TestSet ret = new TestSet(onBotTestName(test),false);
            ret.addTest(test);
            return ret;
        }else{
            return testSets[index];
        }
    }

    /**Gets all annotated classes, makes an instance of them, and files in sets */
    public static abstract class TestScanner implements Scanner {
        public TestScanner() {}
        static Map<ClassLoader,List<OnBotTest>> onBotTests = new HashMap<>();

        static final SearchTarget targets = new TeamCodeSearch();
        AdjacencyRule<Scanner, Graph<Scanner>> loadAdjacencyRule = afterConfiguration();
        AdjacencyRule<Scanner, Graph<Scanner>> unloadAdjacencyRule = beforeConfiguration();

        @Override
        public SearchTarget getTargets(){
            return targets;
        }
        @Override
        public void beforeScan(ClassLoader loader){
            onBotTests.putIfAbsent(loader,new ArrayList<OnBotTest>());
        }

        @Override
        public void scan(ClassLoader loader, Class<?> cls){
            if(cls.isAnnotationPresent(OnBotTestInfo.class) && cls.getSuperclass() == OnBotTest.class && onBotTests.containsKey(loader)){
                try {
                    Log.i("OnBotTest","whee");
                    onBotTests.get(loader).add(cls.asSubclass(OnBotTest.class).getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public void afterScan(ClassLoader loader){}

        @Override
        public void beforeUnload(ClassLoader loader){}
        @Override
        public void unload(ClassLoader loader, Class<?> cls){}

        @Override
        public void afterUnload(ClassLoader loader){
            onBotTests.remove(loader);
        }
    }
    void getAnnotatedClasses(){
        for (ClassLoader i : TestScanner.onBotTests.keySet()) {
            List<OnBotTest> tests = TestScanner.onBotTests.get(i);
            for (int z = 0; z < tests.size(); z++) {
                OnBotTest currentTest = tests.get(z);
                currentTest.hardwareMap = hardwareMap;
                currentTest.telemetry = new TestTelemetry();
                currentTest.gamepad = gamepad2;

                onBotTests.add(currentTest);

                OnBotTestInfo annotation = currentTest.getClass().getAnnotation(OnBotTestInfo.class);


                //add to a testSet
                boolean addedToSet = false;
                assert annotation != null;
                if (!annotation.testSetName().isEmpty()) {
                    for (TestSet set : testSets) {
                        if (set.name.equals(annotation.testSetName())) {
                            set.addTest(currentTest);
                            addedToSet = true;
                        }
                    }
                }
                if (!addedToSet) {
                    generalTests.addTest(currentTest);
                    addedToSet = true;
                }
            }
        }
    }

    /**Runs a menu-like interface */
    void runMenu(String header, String[] choices, String[] footerControls, String[] footerActions){
        uiTelemetry.setHeader(header);
        uiTelemetry.setBody("");
        for(int i=0;i<choices.length;i++){
            if(menuSelected==i){
                uiTelemetry.addBodyLine("-- [["+choices[i]+"]]");
            }else{
                uiTelemetry.addBodyLine("-- "+choices[i]);
            }
        }
        uiTelemetry.setFooterControls(footerControls,footerActions);
        if(gamepad1.dpadDownWasPressed()){
            menuSelected = menuSelected == choices.length-1?0:menuSelected+1;
        }else if(gamepad1.dpadUpWasPressed()){
            menuSelected = menuSelected == 0?choices.length-1:menuSelected-1;
        }

        if(gamepad1.triangleWasPressed()){
            state = State.TO_QUIT;
            stateChanged = true;
        }
    }
}
