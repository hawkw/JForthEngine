( A simple program to demonstrate that the ForthEngine works, by adding two numbers. )
( Now featuring strings and prompting!)

S" Please enter the first number " 0111
S" Please enter the second number " 0112
S" The result is: " 0113

FETCH 0111 COUT CIN 
FETCH 0112 COUT CIN 
ADD STORE 0100 ( store the value at 0x0100 )
FETCH 0113 COUT
FETCH 0100 COUT  ( fetch and print the value from 0x0100 ) 
EXIT 
