# Multi Project Focus [MPF]

**PROJECT IS UNDER DEVELOPMENT, AT AN EARLY STAGE**  

New features are added regularly.  
The first release is planned for the end of February 2026.

# Core idea

The app enables ultra-fast access to your project folders and files by using keyboard shortcuts.  
Wherever you are, whatever you are doing - one shortcut brings you back to your project.

For example:

Assume someone / something interrupts your work. You engage in this interruption and pause works on the project.  
In the meantime you completely lose focus on your original project.  
Then you press:
- `Ctrl + Alt + F` - immediately open the **folder** of your current project
- `Ctrl + Alt + 1` - immediately open the **main file** of your current project
- `... or other shortcuts`

*Notes:*

- *The example above is for Windows, but MPF is designed to work on Linux and macOS too.*
- *All is customizable: shortcuts, current project main file...*


# MPF dictionary

At the start, let us define key terms:  

- `project` - any folder on your disk.  
It can be any regular folder that MPF can access.


- `current project` - a `project` marked by you as "current".  
You can set any `project` as current at any time.


- `pinned projects` - a short list of projects that you "pin".  
You can access pinned projects very quickly from shortcuts.


- `project file`  - a file in project folder known by MPF.  
Of course in `project`, you can keep any files.
But some files can be marked as `File1`, `File2`, `File3`, etc.  
You can quickly open them from anywhere using shortcuts.

For example, your folders on disk / projects can look like this:  
*Note: example shows Windows format, but MPF works also on Linux, and macOS.*

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
With MPF you can quickly switch between these projects / folders.  
No more wasting time / energy / focus  on manual searching for projects.


# Typical use cases

- Fast re-focus on a project after distraction  
Example:  
  - Something interrupts you (phone call, issue in another project, random thought, etc.).
  - You handle distraction.
  - Then you very quickly return your current project - by pressing `Ctrl + Alt + 1`


- Fast temporary jump to another project
  - Assume you are working simultaneously on projects:
  ```text
  [1] product-A-roadmap
  [2] product-B\new-feature-abc
  [3] product-B\bugfix-problem-123
  ```
  - Let's assume you set `1` as current and `2`, `3` as pinned.
  - Most effort stays in project `1`, but when you need `2` or `3`, you jump into them in one click and return back.


# How it works

- MPF works on Windows, Linux, and macOS (thanks to Kotlin Multiplatform).
- MPF works in two modes:
  - no UI mode
  - UI mode
- The mode is selected automatically by MPF.


You "tell" MPF what to do by running commands.  
For example:  

- To handle current project actions:
  - `MultiProjectFocus.exe "ProjectCurrent.OpenFolder"`
  - `MultiProjectFocus.exe "ProjectCurrent.OpenFile(fileNr:2)"`


- To handle pinned project actions:
  - `MultiProjectFocus.exe "ProjectPinned(pinPosition:1).OpenFolder"`
  - `MultiProjectFocus.exe "ProjectPinned(pinPosition:1).OpenFile(fileNr:2)"`


These commands are usually called by an external hotkey tool in your system (for example, AutoHotkey on Windows).


Usually, a command runs in the background without UI, but if user interaction is needed, UI appears automatically.  
For example:  
- If a user tries to open a `project folder` or `project file` that does not exist  
- Then the app can ask whether to create it and continue.
