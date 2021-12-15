# Simple search program
Program that performs multiple searches in multiple text lines.

The program process the command line argument --data followed by the name of the file with the data, 
for example, --data text.txt.

Note that the file should not include the total number of lines. 
All lines read only once, at the start the program.
### Example
The lines that start with > represent the user input. Note that these symbols are not part of the input.
```
=== Menu ===
1. Find a person
2. Print all persons
0. Exit
> 1

Select a matching strategy: ALL, ANY, NONE
> ANY

Enter a name or email to search all matching people.
> Katie Erick QQQ

3 persons found:
Katie Jacobs
Erick Harrington harrington@gmail.com
Erick Burgess
```