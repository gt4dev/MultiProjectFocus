; ==============================
; keyboard shortcuts in AHK (v2)
; ==============================

#SingleInstance Force

mpfExePath := A_ScriptDir ".\..\appBin\main\app\MultiProjectFocus\MultiProjectFocus.exe"
if !FileExist(mpfExePath) {
    MsgBox "Cannot find: " mpfExePath, "Error", 16
    ExitApp
}


; CapsLock l -> load initial projects from file
CapsLock & l::
{
    projectsTomlPath := A_ScriptDir "\..\assets\projects.real.toml"
;    Loop Files projectsTomlPath
;        projectsTomlPath := A_LoopFileFullPath
    RunMultiProjectFocus("LoadInitialData(file:" projectsTomlPath ")")
}

; CapsLock o -> open projects file in system default editor
CapsLock & o::
{
    projectsTomlPath := A_ScriptDir "\..\assets\projects.real.toml"
;    Loop Files projectsTomlPath
;        projectsTomlPath := A_LoopFileFullPath
    Run projectsTomlPath
}


; CapsLock f -> opens: current project / folder
CapsLock & f::
{
    RunMultiProjectFocus("ProjectCurrent.OpenFolder")
}

; CapsLock 1 -> opens: current project / file1 (default = main.md)
CapsLock & 1::
{
    RunMultiProjectFocus("ProjectCurrent.OpenFile(file:F1)")
}

; CapsLock 2 -> opens: current project / file2 (default = dists.md)
CapsLock & 2::
{
    RunMultiProjectFocus("ProjectCurrent.OpenFile(file:F2)")
}

; CapsLock 3 -> opens: current project / file3 (default = dists.md)
CapsLock & 3::
{
    RunMultiProjectFocus("ProjectCurrent.OpenFile(file:F3)")
}


RunMultiProjectFocus(arg)
{
    global mpfExePath
    Run '"' mpfExePath '" "' arg '"'
}
