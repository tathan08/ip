# Jinjja User Guide

Jinjja is a **desktop app for managing your personal tasks**, optimized for use via a **Command Line Interface** (CLI) while still having the benefits of a **Graphical User Interface** (GUI). If you can type fast, Jinjja can get your task management done faster than traditional GUI apps.

## Command summary

| Action | Format, Examples |
|--------|------------------|
| **Add Todo** | `todo DESCRIPTION` <br> e.g., `todo read book` |
| **Add Deadline** | `deadline DESCRIPTION /by DATE_TIME` <br> e.g., `deadline submit project /by 2024-12-15 23:59` |
| **Add Event** | `event DESCRIPTION /from START_DATE_TIME /to END_DATE_TIME` <br> e.g., `event meeting /from 2024-12-10 14:00 /to 2024-12-10 16:00` |
| **Add Tentative** | `tentative DESCRIPTION /slots /from DATE_TIME /to DATE_TIME [/from DATE_TIME /to DATE_TIME]...` <br> e.g., `tentative discussion /slots /from 2024-12-12 10:00 /to 2024-12-12 12:00` |
| **Confirm Tentative** | `confirm TASK_NUMBER SLOT_NUMBER` <br> e.g., `confirm 3 1` |
| **List** | `list` |
| **Mark** | `mark TASK_NUMBER` <br> e.g., `mark 3` |
| **Unmark** | `unmark TASK_NUMBER` <br> e.g., `unmark 1` |
| **Find** | `find KEYWORD` <br> e.g., `find book` |
| **Delete** | `delete TASK_NUMBER` <br> e.g., `delete 3` |
| **Exit** | `bye` |

### **Notes about the command format:**

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `todo DESCRIPTION`, `DESCRIPTION` is a parameter which can be used as `todo read book`.

* Date and time should be in `yyyy-MM-dd HH:mm` format.<br>
  e.g. `2024-12-25 14:30`

* Parameters must be in the specified order for each command.

* Extraneous parameters for commands that do not take in parameters (such as `list` and `bye`) will be ignored.<br>
  e.g. if the command specifies `list 123`, it will be interpreted as `list`.

## Features

### Adding a todo task: `todo`

Adds a todo task to your task list.

Format: `todo DESCRIPTION`

Examples:

* `todo read book`
* `todo finish homework`

Expected output:
```text
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
```

### Adding a deadline task: `deadline`

Adds a deadline task with a specific due date to your task list.

Format: `deadline DESCRIPTION /by DATE_TIME`

* The date and time must be in `yyyy-MM-dd HH:mm` format.

Examples:
* `deadline submit project /by 2024-12-15 23:59`
* `deadline pay bills /by 2024-11-30 17:00`

Expected output:
```
Got it. I've added this task:
  [D][ ] submit project (by: Dec 15 2024, 11:59PM)
Now you have 2 tasks in the list.
```

### Adding an event task: `event`

Adds an event task with a specific start and end time to your task list.

Format: `event DESCRIPTION /from START_DATE_TIME /to END_DATE_TIME`

* Both start and end date/time must be in `yyyy-MM-dd HH:mm` format.
* The start time must be before the end time.

Examples:
* `event team meeting /from 2024-12-10 14:00 /to 2024-12-10 16:00`
* `event conference /from 2024-11-25 09:00 /to 2024-11-25 17:00`

Expected output:
```
Got it. I've added this task:
  [E][ ] team meeting (from: Dec 10 2024, 2:00PM to: Dec 10 2024, 4:00PM)
Now you have 3 tasks in the list.
```

### Adding a tentative event: `tentative`

Adds a tentative event with multiple possible time slots to your task list. This is useful when you want to schedule an event but haven't confirmed the exact time yet.

Format: `tentative DESCRIPTION /slots /from DATE_TIME /to DATE_TIME [/from DATE_TIME /to DATE_TIME]...`

* You can specify multiple time slots by repeating the `/from` and `/to` parameters.
* All date/time must be in `yyyy-MM-dd HH:mm` format.
* At least one time slot must be provided.

Examples:
* `tentative project discussion /slots /from 2024-12-12 10:00 /to 2024-12-12 12:00 /from 2024-12-13 14:00 /to 2024-12-13 16:00`
* `tentative doctor appointment /slots /from 2024-11-20 09:00 /to 2024-11-20 10:00`

Expected output:
```
Got it. I've added this tentative event:
  [TE][ ] project discussion
    Slot 1: Dec 12 2024, 10:00AM to Dec 12 2024, 12:00PM
    Slot 2: Dec 13 2024, 2:00PM to Dec 13 2024, 4:00PM
Now you have 4 tasks in the list.
```

### Confirming a tentative slot: `confirm`

Confirms one of the time slots for a tentative event, converting it to a confirmed event.

Format: `confirm TASK_NUMBER SLOT_NUMBER`

* The task number refers to the index number shown in the displayed task list.
* The slot number refers to the slot you want to confirm (1, 2, 3, ...).
* Both numbers must be positive integers.

Examples:
* `confirm 4 1` (confirms slot 1 of task 4)
* `confirm 2 3` (confirms slot 3 of task 2)

Expected output:
```
Nice! I've confirmed slot 1 for your tentative event:
  [TE][ ] project discussion (confirmed: Dec 12 2024, 10:00AM to Dec 12 2024, 12:00PM)
```

### Listing all tasks: `list`

Shows a list of all tasks in your task list.

Format: `list`

Expected output:
```
Here are the tasks in your list:
1. [T][ ] read book
2. [D][ ] submit project (by: Dec 15 2024, 11:59PM)
3. [E][ ] team meeting (from: Dec 10 2024, 2:00PM to: Dec 10 2024, 4:00PM)
4. [TE][ ] project discussion (confirmed: Dec 12 2024, 10:00AM to Dec 12 2024, 12:00PM)
```

### Marking a task as done: `mark`

Marks the specified task as completed.

Format: `mark TASK_NUMBER`

* The task number refers to the index number shown in the displayed task list.
* The task number must be a positive integer 1, 2, 3, …​

Examples:
* `list` followed by `mark 2` marks the 2nd task in the task list as done.

Expected output:
```
Nice! I've marked this task as done:
  [T][X] read book
```

### Unmarking a task: `unmark`

Marks the specified task as not completed.

Format: `unmark TASK_NUMBER`

* The task number refers to the index number shown in the displayed task list.
* The task number must be a positive integer 1, 2, 3, …​

Examples:
* `list` followed by `unmark 1` marks the 1st task in the task list as not done.

Expected output:
```
OK, I've marked this task as not done yet:
  [T][ ] read book
```

### Finding tasks: `find`

Finds tasks whose descriptions contain the given keyword.

Format: `find KEYWORD`

* The search is case-insensitive. e.g `book` will match `Book`
* Only the task description is searched.
* Partial word matches are supported. e.g. `proj` will match `project`

Examples:
* `find book` returns tasks containing "book"
* `find project` returns tasks containing "project"

Expected output:
```
Here are the matching tasks in your list:
1. [T][ ] read book
2. [D][ ] submit project (by: Dec 15 2024, 11:59PM)
```

### Deleting a task: `delete`

Deletes the specified task from your task list.

Format: `delete TASK_NUMBER`

* Deletes the task at the specified `TASK_NUMBER`.
* The task number refers to the index number shown in the displayed task list.
* The task number must be a positive integer 1, 2, 3, …​

Examples:
* `list` followed by `delete 2` deletes the 2nd task in the task list.

Expected output:
```
Noted. I've removed this task:
  [D][ ] submit project (by: Dec 15 2024, 11:59PM)
Now you have 3 tasks in the list.
```

### Exiting the program: `bye`

Exits the program.

Format: `bye`

Expected output:
```
Bye. Hope to see you again soon!
```

### Saving the data

Jinjja data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

The data file is saved as `ip/data/jinjja.txt` in the same folder as your Jinjja application.
