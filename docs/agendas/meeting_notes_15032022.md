## Meeting notes 15.03.2022

---

## What we talked about
- Everyone describes the work they did during the past (two) week/s
- Show current state of the application

## Deadlines
- Assignment 5B - **April 1** (individual) 

## Design choices
- Should a question always run to the end of the time or should it be done when everybody has answered?
    * Right now it always runs to the end and it's possible to change the answer during that time
    * Keep it as it is right now, can be tweaked later
- Where should the admin control menu button go on the home screen?
    * Create a settings screen which includes dark mode and button to admin controls. Can also include the input for the server URL
- Where should the singleplayer scoreboard be shown?
    * If server URL input is moved to settings, then scoreboard can be on the home screen. Otherwise add another button to the home screen. To be decided which to do.

## Work distribution
- Agree ahead of time what needs to be done and assign it

## Questions
- Is there anything else we can do to improve our process grade? Are there any GitLab features we are not using (at all/correctly)?
    * See below in gitlab section
- Is there a fixed date when we are going to receive our grade from the HCI report?
    * HCI Grade will be published at the end of the course

## Feedback
### Gitlab
- Gitlab milestones should be per sprint (week). Should be named accordingly.
- Use gitlab assign task feature. Important for process grade
- A task (gitlab issue) should only be done by one person
- Use gitlab's estimate and time spent features
    * Used by writing /estimate or /spend in the issue/merge request description followed by a time (for example 30m, 2h)
- Boards tab could be overhauled to better represent what tasks are assigned, what aren't.

### Code
- Every public method should include javadoc
    * Solution: Fix what we have and add a checkstyle check
    * Solution: Change as many methods to private as possible
- Test coverage is bad
    * Testing the client is difficult, no need to focus on that too much right now
    * Tests in commons and server should cover almost everything (Including getters, setters, equals, etc)
    * Solution: Use lombok for getters, setters, equals, etc. If the methods aren't written out then there's nothing to test ;)

## To do for this week
- Wednesday morning meeting for task distribution
    * Finish must have issues (check what's done and what's not)
    * Start work on some should have issues
    * Fix problems mentioned in the feedback section
- Coding time!
