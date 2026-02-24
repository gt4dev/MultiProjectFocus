# Multi Project Focus [MPF]

**Project is under heavy development.**  
Core features work. New features are added regularly.

# Table of Contents

- [Core idea](#core-idea)
- [MPF dictionary](#mpf-dictionary)
- [Sample use cases](#sample-use-cases)
- [Installation](#installation)
- [Usage](#usage)
- [Others](#others)

# Core idea

The app enables ultra-fast access to your project folders and files using keyboard shortcuts.  
Wherever you are and whatever you are doing - one shortcut brings you back to your project.

For example:

Assume someone or something interrupts your work. You handle the interruption and pause work on the project.  
In the meantime, you completely lose focus on your original project.  
Then you press:
- `CapsLock + F` - it immediately opens the **folder** of your current project
- `CapsLock + 1` - it immediately opens the **main file** of your current project
- `...or other shortcuts`

*Notes:*

- *The example above is for Windows, but MPF is designed to work on Linux and macOS too.*
- *Everything is customizable: shortcuts, current project,  main file, etc.*


# MPF dictionary

At the start, let's define key terms:  

- `project` - any folder on your disk.  
It can be any regular folder that MPF can access.


- `current project` - a `project` that you mark as "current".  
You can set any `project` as current at any time.


- `pinned projects` - a short list of projects that you "pin".  
You can access pinned projects very quickly from shortcuts.


- `project file` - a file in a project folder known by MPF.  
Of course, in a `project` folder, you can keep any files.  
But some files can be marked as `File1`, `File2`, `File3`, etc.  
You can quickly open them from anywhere using shortcuts.

For example, your folders/projects on disk can look like this:  
*Note: this example uses Windows path format, but MPF also works on Linux and macOS.*

```text
c:\projects\
            home\
                 family-vacations
                 prepare-house-painting
                 mini-tasks
            work\
                 product-A-roadmap
                 product-B\
                          new-feature-abc
                          bugfix-problem-123
            free-ideas\
                       important
                       others
c:\cloud-drive\shared\
                      team-A\project-X
                      team-B\project-Y
```
With MPF, you can quickly switch between these projects/folders.  
No more wasting time, energy, or focus on manually searching for projects.


# Sample use cases

- Fast refocus on a project after a distraction  
Example:  
  - Let's assume something interrupts your current project work: a phone call, an issue in another project, a random thought, etc.
  - You handle the distraction.
  - Then you can quickly refocus on your main project: by pressing `CapsLock + 1`, you open the `current project's main file`.


- Fast temporary jump to another project
  - Let's assume:
    - you are working simultaneously on projects:
      ```text
      [1] product-A-roadmap
      [2] product-B\new-feature-abc
      [3] product-B\bugfix-problem-123
      ```
    - you set `1` as current and `2`, `3` as pinned.
  - Most effort stays in project `1`, but when you need `2` or `3`, you jump into them in one click and return to the current project.



# Installation

## Build MPF

The app is provided as source code to build yourself.  
This helps you ensure the app **does not read** your files or any data, thus is **completely safe for your use**.

1. Download the source code.
2. In the project root folder, run command:

    `gradlew createDistribution`  

    This compiles the sources and generates the app for your current platform.   
    For example: `.exe` for Windows, `.app` for macOS, and a binary file for Linux.
3. The generated app file is placed in `./appBin`.

## Install a hotkey manager

Note: this section describes using MPF with AutoHotkey, but you can use any hotkey manager available on your OS.

1. Install [AutoHotkey](https://www.autohotkey.com/).
2. Run [the script for AutoHotKey](./AutoHotKey/MultiProjectFocus.ahk), that maps hot key shortcuts to MultiProjectFocus commands.  
   Recommendation: run this script at system startup.  
   In Windows: 1/ press `Win + R`, 2/ type `shell:startup` and 3/ create a shortcut to [MPF AutoHotkey config](./AutoHotKey/MultiProjectFocus.ahk).

# Usage

Using MPF boils down to calling the app with a proper parameter that says which project and which file or folder to open.

Here is the list of currently supported commands.  
Note: examples are for Windows, but any OS will work.

- `ProjectCurrent.OpenFolder`  
  Opens the current project folder.  


- `ProjectCurrent.OpenFile(file:F5)`  
Opens current project file number 5.


- `ProjectPinned(pinPosition:3).OpenFolder`  
Opens the folder of the project pinned at position 3.


- `ProjectPinned(pinPosition:3).OpenFile(file:F7)`  
Opens file number 7 of the project pinned at position 3.


- `LoadInitialData(file:c:\path\to\folder\with\init-config.toml)`  
Populates the MPF database with projects defined in a TOML file.  
This file defines which project is current and which projects are pinned.


Each command is passed to MPF by the app start parameters.  
Samples:
```bash
MultiPorojectFocus.exe "ProjectCurrent.OpenFile(file:F5)"
MultiPorojectFocus.exe "ProjectPinned(pinPosition:3).OpenFile(file:F7)"
```


Below table list hot key [definied here](./AutoHotKey/MultiProjectFocus.ahk), ready to use to call MPF commands.  
So you can quickly access any of your project.

| Shortcut                                  | Action                                                |
|-------------------------------------------|-------------------------------------------------------|
| **Current project commands**              |                                                       |
| `CapsLock + F`                            | Open current project folder                           |
| `CapsLock + 1`                            | Open current project file 1                           |
| `CapsLock + 2`                            | Open current project file 2                           |
| `CapsLock + 3`, `..4`, `..5`              | Open current project files 3, 4, 5                |
| **Pinned project commands**               |                                                       |
| `CapsLock + P, {pin position}, F`         | Open pinned project `{pin position}` folder           |
| `CapsLock + P, {pin position}, {file nr}` | Open pinned project `{pin position}` file `{file nr}` |
| *examples of above*                       |                                                       |
| `CapsLock + P, 1, 2`                      | Open pinned project 1 file 2                          |
| `CapsLock + P, 3, 5`                      | Open pinned project 3 file 5                          |
| `CapsLock + P, 4, F`                      | Open pinned project 4 folder                          |
| **Load data into MPF commands**                   |                                                       |
| `CapsLock + L`                            | Populate MPF database from a TOML file                |
| `CapsLock + O`                            | Open TOML file for editing                            |

# Others

## Your data and privacy safety

Your safety is a primary MPF concern.  
MPF has opened sources, so you can check every part of its code.  
Your project files are not read nor processed by MPF.

## License

You can modify this project in any way you need, but only for your personal, private, or professional needs.  
You are not allowed to modify the code and publish it as your own.  
You can distribute the original sources as they are, without modifications.

## Contact

In case of problems or unclear cases, please email me or report an issue in the project.  
It is valuable to me because it gives feedback on what must be refined.


## What next

The project is currently under heavy development and is still at an early stage.  
Please stay tuned for further improvements.  
The UI will be added in the next steps. 