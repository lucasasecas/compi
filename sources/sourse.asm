.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
include \masm32\include\masm32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
includelib \masm32\lib\masm32.lib
.data
_enterobienla	DW	0
_error db "No se puede dividir por cero", 0
.code
start:
JMP _start
label_div0Excp:
invoke MessageBox, NULL, addr _error, addr _error, MB_OK
JMP label_quit
_start:
mov ax, 33
mov _enterobienla, ax


label_quit:
invoke ExitProcess , 0
end start
