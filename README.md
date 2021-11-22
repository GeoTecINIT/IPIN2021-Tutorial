# IPIN 2021 Tutorial - Battling the Android scheduling system: how to reliably schedule background measurements in Android

This repository contains all the code and instructions to develop a simple periodical Bluetooth Low Energy scanning
Android application. 

In this case, the application will scan for BLE devices during ten seconds every minute. The difficult part
is getting the app to run in a systematic (i.e., every minute, with minimum delay) and a reliable way (i.e, working for weeks, months, etc...)

During this tutorial, we will visit several tasks scheduling approaches, developing
from the least reliable and simple solution (i.e., the one any newcomer or without experience developer would apply)
 to the most robust and engineered one.
 

## Repository structure

This repository is structured using *git tags*. Each one of the proposed solutions is tagged in this way:

- v*-sp (starting point): this version contains the instructions (as TODO comments) to develop the version v*.
- v*-sol (solution): this version contains a valid solution for the version v*.

### Proposed solutions (versions)

The proposed solutions, from worst to better are the following ones:

- **v0 - The simple way**. It uses the `AlarmManager#setRepeating(...)` method to schedule the scans every minute and a `BroadcastReceiver` to execute the scan itself.
- **v1 - Rescheduling**: Due to the inaccuracy of `AlarmManager#setRepeating(...)`, a new approach has to be used. 
We choose to schedule a single alarm with `AlarmManager#set(...)` and to reschedule the alarm again in the `BroadcastReceiver` when it triggers.
- **v2 - Behaviour changes over time**: We have used `AlarmManager#set(...)`, but it turns out that after a SO version that method is not reliable anymore. Instead, Android tells us to use
`AlarmManager#setExact(...)`, which does what the previous method is not able to do anymore. And then the DOZE mode arrives and `AlarmManager#setExact(...)` is not exact anymore. Fortunately, the merciful Android
 provides us with `AlarmManager#setExactAndAllowWhileIdle(...)`, which does what the previous method is not able to do anymore... So now, depending on the Android version one method has to be used over another.
- **v3 - Services are better**: `BroadcastReceiver`s are nice, the problem is that we have a limited time to do our things on them. If we take to long, Android kills the execution of the receiver. Then, we need
to do our things in a safer place, such a background `Service`. 
- **v4 - Move it to foreground**: Background services are good, but we are still not safe. What pisses of Android are developers who do things in their apps without user's knowledge (i.e., background work). Moreover, Android
will crash the app if we try to access some resources (e.g., location, camera, etc...) without telling it to the user. Foreground services are here to save us. These services show a notification while they are being executed,
so then the user is aware of them and Android is happy.
- **v5 - Keep it awake**: Android is really mad about battery savings to the point of restricting the access to the CPU. So Android is perfectly capable of reducing the CPU assignment for our foreground service if it is 
executed frequently. Fortunately, we are able to stick to the CPU and not let it go thanks to the `WakeLock`.

## Installation and usage instructions

### Requirements:
- Laptop with Internet connection and a [Git client](https://git-scm.com/downloads)
- Android Studio
- Android Phone (minimum version Android 4.3)

### Download the repository

To download the repository in your machine, starting from the first proposed solution, you can execute:
```
$ git clone https://github.com/GeoTecINIT/IPIN2021-Tutorial.git -b v0-sp
```


### Tags usage

You can also list the different available versions executing the next command:
```
$ git tag
# Output
v0-sol
v0-sp
v1-sol
v1-sp
v2-sol
v2-sp
v3-sol
v3-sp
v4-sol
v4-sp
v5-sol
v5-sp
```

Then, you can move through the repository to explore the different versions using the following command:
```console
$ git checkout <tag>
```

### Proposed workflow

- Go to the **v0 - The simple way** version starting point by executing the following command:
```console
git checkout v0-sp
```
Not needed if you downloaded the repository using the command presented in [Download the repository](#download-the-repository)

- Follow the `TODO` comments to implement the proposed version.
- (Optional) Commit your changes in a new branch. 
```
git checkout -b my-v0-sol
git add *
git commit -m "my v0 solution"
```
If you don't do this step, you could lose your changes in the next step.

- Check our proposed solution:
```
git checkout v0-sol
```

- Repeat for the remaining versions.