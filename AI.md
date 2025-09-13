# Usage of AI

| Tool Used | Task Given | Observations |
|---|:---:|---:|
| Copilot | Shorten parseTentativeCommand() and maintain SLAP for Parser.java | When given specific instructions (i.e. to act on a single method), Copilot tends to focus on those specific instructions without understanding the context. For example, it made a new method findSlotsIndex() to find delimiter "/slots" instead of creating a general method findIndex() / using existing methods to find delimiters by passing in a string parameter. |
| Claude | Refactor saveTasksToFile() and maintain SLAP for Storage.java | Claude gave very clean and organised code, but took around 5 iterations to get the code perfect. This is because it could not run the gradle build tests without being connected to the IDE. As a result, there were some errors it made by assuming class and instance methods, since it did not have access to the whole codebase. |
