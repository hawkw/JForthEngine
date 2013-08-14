( A simple program to demonstrate that the ForthEngine works, by adding two numbers. )
CIN STORE 0010 ( Store the input at 0x0010)
CIN STORE 0011 ( These memory locations chosen completely arbitrarily)
FETCH 0010
FETCH 0011 
ADD STORE 0012
FETCH 0012 COUT
EXIT
