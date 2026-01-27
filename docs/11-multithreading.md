## #11 Utilize Multithreading to speed up thumbnail loading
#### Branch name: 11-multithreading
[Back](../TODO.md)

### Task Description:
You are to get the program working with multithreading. The following features need to be added:
- MultiThread image rendering logic.
- Implement a loading bar or status text at the bottom of the root BorderPane.

### New Notes 1/27/26:

You also may need to multithread the fileOpenFolder.setOnAction logic for choosing the files.
Choosing files seems to use most CPU time rather than rendering the images themselves.
Use the debugger to assess the situation.

Also, make a new variable in Main with a getter and setter that has a boolean for faded list rows.