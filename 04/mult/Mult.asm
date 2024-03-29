// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// Assumes that R0 >= 0, R1 >= 0, and R0 * R1 < 32768.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
//start iterator
@i
M=1
//sum=0
@sum
M=0

(LOOP)
    //if (i>R1) GOTO stop
    @i
    D=M
    @1
    D=D-M
    @STOP
    D;JGT
//sum = sum + R0
    @sum
    D=M
    @0
    D=D+M
    @sum
    M=D
    //i++
    @i
    M=M+1
    //goto LOOP
    @LOOP
    0;JMP
(STOP)
    @sum
    D=M
    @2
    M=D
(END)
    @END
    0;JMP

