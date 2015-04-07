GreenAdvisor
============

This file explains the requirements and instructions to run of Green Advisor. 


This is a Java based tool and has been tested on Ubuntu, Windows, and MacOS platforms.
Green Advisor advises Android Application developers on Change in energy consumption 
of their app with change in app code, based on system call tracing. 

CONFIGURATION
-------------
Please extract the tool zip in your Github project directory. 

a) Green Advisor needs four paths to be set up in order to work. These are:

	1) adbPath: The path to ADB(Android Debug Bridge) that comes with your adt bundle 
	and would be Android SDK folder's platform tools. 
	Eg for the lab computers: adbPath= /usr/local/share/android-sdk-linux/platform-tools/adb

	2) gitDir: This is the path to your Github directory you are using for the Android Application 
	project. 
	Eg: gitDir= /cshome/kaggarwa/karan/lonelyTwitter/

	3) appCodeDir: This is the path to the directory containing your Application code(i.e. where
	your App's AndroidManifest.xml is stored). It may or may not be same as your Github directory.
	Eg: appCodeDir= /cshome/kaggarwa/karan/lonelyTwitter/lonelyTwitter/
	

	4) testCodeDir: This is the path to the directory containing your Android Junit tets code(i.e. where
	your Jnuit test's AndroidManifest.xml is stored). It would be some where in your Github directory. 
	Eg: appCodeDir= /cshome/kaggarwa/karan/lonelyTwitter/lonelyTwitterTests/
	

b) The system should have a valid Java installation and version >=*1.6*

How to Run
-----------

1. Please have your Android emulator **running** before starting the Green Advisor.
2. Please **make sure** that you have run the junit tests **prior** to using this tool, so that apk files required by the tool are generated. Or else, the tool could be using old apk files, giving you wrong advise. 
3. Once you have set up the Configuration, open Terminal/CMD, and then go to the jar directory in the
Green Advisor directory.

        Eg: cd /cshome/kaggarwa/karan/GreenAdvisor/jar/

4. Then run the following command:

        java -cp tool.jar strace.callStrace

Alternatively for Steps 3 and 4, you can remain in GreenAdvisor directory, and execute:

        sh run.sh
        
This will execute the Green Advisor that will run your Junit tests on the Application.
If being run for the first time it will simply tell that it has no history of your App,
and hence, will not be able to give any recommendation.

When you change your application next time, and have commited it to your repository,
you can use Green Advisor's recommendation about your Application's energy change
by following the same above steps to run the tool. It will generate a HTML report 
in the Green Advisor Directory, i.e. a file named report.html:
        Eg: /cshome/kaggarwa/karan/GreenAdvisor/report.html

This report will give the system call change data, and list the calls that have changed
significantly with their operations. It then gives the recommendation whether the 
application's energy consumption has changed or not. It also displays the Git code 
diff between two versions.

Please note: 
It only shows the difference between the last recorded version with the Green Advisor
and the latest version on the Git repo; it won't  show any data for the 
intermediate versions.

Options
-------
To run and compare the difference between two versions identified by their
Commit SHAs:
Run the following command in the Step 3 of the How to run section:

     java -DversOne=CommitSHAofVers1 -DversTwo=CommitSHAofVers2  -cp tool.jar strace.callStrace 

     eg: java -DversOne=2de3hfi5sn3v3ewd2 -DversTwo=e2e2b2ue24nefi3i -cp tool.jar strace.callStrace  

This will generate the report based on these two commits.
PS: Please make sure that you have already run the tool for these two commits before using this option.

Troubleshooting
---------------

1. Check whether the paths in the SetVariables file are set correctly.
2. You might be running the tool without having any Android Junit Tests.
3. Check whether the Jnuit Project has the .apk file in the bin/ folder.

In case of any issue, or help, contact me at **kaggarwa@ualberta.ca**


CMPUT 301 LAB DEMO
------------------

Please find the presentation [here](Green_Advisor_Lab_demo.pdf). 

See the sample report [here](sample_report.html).

The Energy Consumption demo in the lab was just for demonstartion purpose, you don't need to run it for the feedback questionnaire.
Only the GreenAdvisor tool runs are required to complete the Questionnaire.

CMPUT 301 Feedback Questionnaire
--------------------------------

For the Q2, submitting that report under the GreenAdvisor folder of your project repos would be good enough. 
If you choose to do so, then just add the CommitID SHAs in the Q2 , no need to write the code snippet on the sheet you are submitting.

If you want to download the Feedback_Questionnaire, its [here](Feedback_Questionnaire.docx)

LICENSE
-------

(c) 2014 Karan Aggarwal 

Under LGPL Version 3, please see LICENSE
