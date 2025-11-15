# Android_14_Demos

Open this README.md file in VS Code; otherwise, it will look messy in the text editor.

==================================================================================================================================================================
																		Demo 1

																The Android Framework



																1-Create a binder interface


Creating a service involves several steps. First, we need to define the Binder interface, using
AIDL, then create a manager shim for it, and finally implement the service itself. In this case, the
service will be encapsulated in a system application that has UID system, which is necessary to
register a service, and is persistent so that it will be started at boot-time. This diagram shows the
components:

Note about the Manager Shim:
The manager shim acts as a lightweight wrapper around the Binder interface. It hides the low-level AIDL and IPC details and provides a clean, user-friendly API for clients. This ensures easier access to the service, better abstraction, and safer interaction with the underlying system logic.


																									
																									
																																				
																							     	     +-----+
                         																		         | App |
                      																		             +-----+
																									        |
																									     	|
																										    ∨
+---------------------------------------------------------+						+---------------------------------------------------------+
|                     simple-service                      |						|                    simple-manager                       |
|                    simple-service.apk                   |						|                 com.example.simplemanager.jar           |
|                                                         |						|                                                         |
|  +-----------------------------------------------+      |						|  +-----------------------------------------------+      |
|  |            simpleServiceApp.java              |      |						|  |              SimpleManager.java               |      |
|  |  - persistent                                 |      |						|  |  - getService("simpleserver")                 |      |
|  |    ld="android.uid.system"                    |      |						|  +-----------------------------------------------+      |
|  |  - addService("simpleserver")                 |      |						|                           |                             |
|  +-----------------------------------------------+      |						+---------------------------|-----------------------------+
|                                                         |													|
|  +-----------------------------------------------+      |													|
|  |            ISimpleServiceImpl.java            |      |													|
|  |  - addInts()                                  |      |													|
|  |  - echoString()                               |      |													|
|  +-----------------------------------------------+      |													|
|                                                         |													|			
+---------------------------^-----------------------------+													|
                            ∧																				|
							|																				|	
                            |  Binder interface ISimpleManager												|
                            |																				|
							|																				|
							|																				|
							↑--------------------------------------------------------------------------------
							
							
Note ->If you do not have the vendor folder create a vendor folder and inside vendor folder create a xrda3 folder in which we put all our demos

 
						Inside the Demos folder we have the  "simple-manager" folder
						copy and paste inside the vendor/xrda3 folder and build it:

# mmm vendor/xrda3/simple-manager

after the build Check that the permissions file is installed in:

[100% 2242/2242] Install: out/target/product/xrda3car/system_ext/framework/com.example.simplemanager.jar

#### build completed successfully (02:04 (mm:ss)) ####



Note ->Check that the extension library is installed in:
$OUT/system_ext/framework/com.example.simplemanager.jar


Check that the permissions file is installed in:
$OUT/system_ext/etc/permissions/com.example.simplemanager.xml



																2-Implement the service




									Inside the Demos folder we have the  "simple-service" folder
									copy and paste inside the vendor/xrda3 folder and build it


# mmm vendor/xrda3/simple-service

build log =>

[100% 210/210] Install: out/target/product/xrda3car/system_ext/app/simple-service/simple-service.apk

#### build completed successfully (01:56 (mm:ss)) ####

xrda3@xrda3:~/aosp_14_training$




after the build check for the Check that the simple-service app is installed in:
$OUT/system_ext/app/simple-service/simple-service.apk




Final Step =>


Add both the manager and service to your device configuration: look at the Android.bp
files for simple-manager and simple-service. Add these packages to to your device.mk file like below.


device/xrda3/xrda3car/device.mk 

open and add the below content:

(To include simple service and manager in the target images, add this to your device.mk)

	PRODUCT_PACKAGES += \
						simple-service \
						com.example.simplemanager



after adding the above Build Android:
# m -j24


																3-Testing
																
Note=> We need the device to be running with SELinux in permissive mode in order to start
simpleservice.											
																
# launch_cvd -start_webrtc -guest_enforce_security=false	

In an ADB shell, check the SELinux mode:

# getenforce    // it will show Permissive
Permissive
			
After booting, you can see that the Simple service app is running, with UID system:
				
# ps -A | grep simple
system        2702   370   13899172  91560 do_epoll_wait       0 S com.example.simpleservice

				
				
Now list the services and check that verify that "simpleservice" is registered:

# service list | grep simple                                                                                                                                                           
251	simpleservice: [com.example.simplemanager.ISimpleManager]
xrda3car:/ # 


# logcat -s SimpleService
--------- beginning of main
11-14 12:02:37.517  2702  2702 D SimpleService: Registered service



	You can call the two interfaces using service call. Interface 1 adds two 32-bit integers:

# service call simpleservice 1 i32 3 i32 6
Result: Parcel(	00000000 00000009   '........')
xrda3car:/ # logcat -s SimpleService                                                                                                                                                                      
--------- beginning of main
11-14 12:02:37.517  2702  2702 D SimpleService: Registered service
11-14 12:09:52.666  2702  2736 D SimpleService: addInts


And above result, the answer is 9. Note that although the command-line integers are in decimal, the contents
of the returned parcel are in hex, so adding 64 and 128 looks like this:

# service call simpleservice 1 i32 64 i32 128
Result: Parcel(	00000000 000000c0   '........')
xrda3car:/ # logcat -s SimpleService                                                                                                                                                                      
--------- beginning of main
11-14 12:02:37.517  2702  2702 D SimpleService: Registered service
11-14 12:09:52.666  2702  2736 D SimpleService: addInts
11-14 12:13:20.094  2702  2736 D SimpleService: addInts


 i. because in hex, 0x40 + 0x80 = 0xc0

	Likewise, you can test that the second interface echos a string
	
# service call simpleservice 2 s16 "Hello world"
Result: Parcel(	
0x00000000: 00000000 0000000b 00650048 006c006c '........H.e.l.l.'
0x00000010: 0020006f 006f0077 006c0072 00000064 'o. .w.o.r.l.d...')
xrda3car:/ # logcat -s SimpleService                                                                                                                                                                      
--------- beginning of main
11-14 12:02:37.517  2702  2702 D SimpleService: Registered service
11-14 12:09:52.666  2702  2736 D SimpleService: addInts
11-14 12:13:20.094  2702  2736 D SimpleService: addInts
11-14 12:15:42.680  2702  2736 D SimpleService: echoString

	Note=> that the 8-bit ASCII string is converted to 16-bit Unicode



	


==================================================================================================================================================================

																		Demo 2


															Android applications and activities

	Objectives:
				Create an application that will call the simple-manager platform library


	1-Applications started at boot time

Find persistent applications:

# adb shell dumpsys package packages > packages.txt


Looking through the file(packages.txt) for applications with the PERSISTENT flag, you should find these

 ___________________________________________________________________________________
| Package/Component                  | Description                                  |
| ---------------------------------- | -------------------------------------------- |
| com.android.networkstack           | Network stack                                |
| android                            | The framework (not an app)                   |
| com.android.car                    | Car service (automotive only)                |
| com.android.ons                    | Opportunistic Network Service                |
| com.android.se                     | Secure element                               |
| com.android.cellbroadcastservice   | Receives network wide broadcasts             |
| com.android.service.ims            | IP multimedia and voice service              |
| com.android.networkstack.tethering | Tethering                                    |
| com.android.phone                  | The phone app                                |
| com.android.systemui               | System UI - notification and navigation bars |
| com.example.simpleservice			 | Your simple system service					|
-------------------------------------------------------------------------------------


	2- Create and Build the sample application

Follow these instructions to build the solution to exercise, which includes the integration with
com.example.simplemanager.jar

Inside the Demos folder we have the  "simple-manager-app" folder
copy and paste inside the vendor/xrda3 folder and build it:

Then, edit [ device/xrda3/xrda3car/device.mk ] and add this.
		 
		 PRODUCT_PACKAGES += simple-manager-app

Then build xrda3

# m -j24

After the build apk file is installed into the target filesystem /system_ext/app:

# ls out/target/product/xrda3car/system_ext/app/simple-manager-app
oat  simple-manager-app.apk



	Note that the application is installed in "/system_ext/app/simple-manager-app/simple-manager-app.apk" and is available in the
	application drawer of the launcher
	if not found simply run the below command and it will launch the application (simple-manager-app.apk)
# am start -S -n com.example.simplemanagerapp/.SimpleManagerActivity

after testing you can stop/close the application using GUI or  using below command also

# am force-stop com.example.simplemanagerapp


	3-Platform libraries - simple manager
	
Note that com.example.simplemanager is a platform library:

# pm list libraries | grep simple                                                                                                                               
  library:com.example.simplemanager

Now, add in the code so that the app can call simple-manager [ In our case we have alredy added so no need to add ]
in Android.bp
    libs: ["com.example.simplemanager"],
    uses_libs: ["com.example.simplemanager"],


Add this to AndroidManifest.xml, inside the application tag: [ In our case we have alredy added so no need to add ]

 <!-- Add this uses-library tag -->
        <uses-library 
            android:name="com.example.simplemanager"
            android:required="true" />
			
			
Build and test on the target
Check that you can call simple manager from the user interface of the app
If you added the third function(Button 3) to simple manager, add code to call it from the app.

==================================================================================================================================================================
