; ==============================
; keyboard shortcuts in AHK (v2)
; ==============================

#SingleInstance Force


mpfExePath := A_ScriptDir ".\..\appBin\main\app\MultiProjectFocus\MultiProjectFocus.exe"
if !FileExist(mpfExePath) {
    MsgBox "Cannot find: " mpfExePath, "Error", 16
    ExitApp
}


; ==================================
; shortcuts for `projects.toml` file
; ==================================

; `CapsLock l` - loads initial projects from file
CapsLock & l::
{
    projectsTomlPath := A_ScriptDir "\..\assets\projects.real.toml"
    RunMultiProjectFocus("LoadInitialData(file:" projectsTomlPath ")")
}

; `CapsLock o` - opens projects TOML file in system default editor
CapsLock & o::
{
    projectsTomlPath := A_ScriptDir "\..\assets\projects.real.toml"
    Run projectsTomlPath
}


; =========================================================
; shortcuts for 'current project' (open folder, open files)
; =========================================================

; `CapsLock f` - opens current project folder
CapsLock & f::
{
    RunMultiProjectFocus("ProjectCurrent.OpenFolder")
}

; `CapsLock+digit (1..9)` - opens current project files 1..9.
Loop 9 {
    digit := A_Index
    Hotkey "CapsLock & " digit, OpenCurrentProjectFile.Bind(digit)
}


; ========================================================
; shortcuts for 'pinned project' (open folder, open files)
; ========================================================

; `CapsLock p, num_A, num_B` - opens pinned project num_A and its file num_B
;    example: `CapsLock p, 2, 4` opens file nr 4 in pinned project nr 2
;
; `CapsLock p, num_A, f` - opens pinned project num_A and its folder
;    example: `CapsLock p, 2, f` opens folder in pinned project nr 2
CapsLock & p::
{
    keySequence := InputHook("L2 T2")
    keySequence.Start()
    keySequence.Wait()
    if RegExMatch(keySequence.Input, "^\d[\dfF]$") {
        keyA := SubStr(keySequence.Input, 1, 1)
        keyB := SubStr(keySequence.Input, 2, 1)
        if (StrLower(keyB) = "f") {
            RunMultiProjectFocus("ProjectPinned(pinPosition:" keyA ").OpenFolder")
        } else {
            RunMultiProjectFocus("ProjectPinned(pinPosition:" keyA ").OpenFile(file:F" keyB ")")
        }
    }
}


OpenCurrentProjectFile(fileNumber, *)
{
    RunMultiProjectFocus("ProjectCurrent.OpenFile(file:F" fileNumber ")")
}


RunMultiProjectFocus(arg)
{
    global mpfExePath
    Run '"' mpfExePath '" "' arg '"'
}
