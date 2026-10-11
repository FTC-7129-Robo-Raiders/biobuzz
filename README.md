## Purpose

This repository contains Robo Raiders team code for the the BIOBUZZ (2026-2027) competition season.

## How Was This Repository Created? 
This repository was originally forked from the [FtcRobotController](https://github.com/FIRST-Tech-Challenge/FtcRobotController)
project to get the core FTC libraries. Then, that fork connection was removed as new changes
should be only merged into our team repository and the fork requires more manual changes every time
a pull request (PR) is created. If new core FTC library changes are wanted, they will need
to be incorporated outside the fork process (e.g. via an additional remote when running locally).

## Getting Started
Consult the [Getting Started](https://github.com/FIRST-Tech-Challenge/FTCRobotController#getting-started)
of the base FTCRobotController document for details on how to get started. It is also useful
to consult the [FTC documentation](https://ftc-docs.firstinspires.org/en/latest/index.html) throughout
robot development.

Things worth knowing for how our team typically makes changes:
1. We use Java in Android Studio for programming our robot; there are other tutorials for Blocks
and OnBot Java in the FTC Docs. However, those will likely not be relevant to you so you can
generally consult [the Android Studio Java docs](https://ftc-docs.firstinspires.org/en/latest/programming_resources/android_studio_java/Android-Studio-Tutorial.html#)
instead.
2. Our team's code can be found in the [TeamCode](./TeamCode) directory. Other directories
may be useful to reference, but we typically are adding files, making changes, etc. only in that
directory.
3. In our code base, we put our top-level OpModes in `opmodes` package of our TeamCode directory.
This allows putting our OpModes in one place and separating them out from other packages for things like
subsystems, utility functions, etc. See the
[TeamCode/src/main/java/org/firstinspires/ftc/teamcode/opmodes](./TeamCode/src/main/java/org/firstinspires/ftc/teamcode/opmodes)
directory for available OpModes.
4. Instructions for our Git workflow to make changes and then merge into the `main` branch are
documented in the [Contributing](https://github.com/FTC-7129-Robo-Raiders/biobuzz/?tab=contributing-ov-file)
tab of our documentation. That is useful to follow for making changes on an isolated branch
and merging to `main` when the changes are ready for use by everyone.