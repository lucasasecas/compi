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
@aux0	DW	0
_b	DW	0
@aux1	DW	0
@aux2	DW	0
_message0 db "no"
_a	DW	0
_message1 db "si"
_error db "No se puede dividir por cero"
.code
start:
JMP _start
label_div0Excp:
invoke MessageBox, NULL, addr _error, addr _error, MB_OK
JMP label_quit
_start:
mov ax, 0
mov _b, ax
mov ax, 0
mov _a, ax
label_7:
mov ax, _a
cmp ax,12
JG label_29
mov ax, _a
add ax, 2
mov @aux0, ax
mov ax, @aux0
mov _a, ax
mov ax, _b
add ax, 1
mov @aux1, ax
mov ax, @aux1
mov _b, ax
mov ax, _a
add ax, 1
mov @aux2, ax
mov ax, @aux2
mov _a, ax
jmp label_7
label_29:
mov ax, _b
cmp ax,5
JNE label_38
invoke MessageBox, NULL, addr _message1, addr _message1, MB_OK
jmp label_40
label_38:
invoke MessageBox, NULL, addr _message0, addr _message0, MB_OK
label_40:

label_quit:
invoke ExitProcess , 0
end start
