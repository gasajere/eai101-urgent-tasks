# EAI 101 — urgent tasks (practice repo)

A practice repository for the EAI 101 afternoon course (Asaba Airport Company interns).
It holds a copy of the morning Week 1 Kotlin project, plus the "urgent tasks" feature from the afternoon lessons.

## Run all tests

- Windows: `.\gradlew.bat cleanTest test`
- Mac: `./gradlew cleanTest test`

## Team rules

1. Never change `main` directly. Make a branch, push it, and open a pull request (PR).
2. Run all tests before you push.
3. A teammate reviews every PR before it is merged.

## Client rules for urgent tasks (agreed 5th Nov 2026)

- Urgent = not done, and overdue or due today or due tomorrow (client change, 26 Oct). A task due in 2 or more days is not urgent.
- Done = never urgent. No due date = never urgent.
- Priority labels: 1 day left = "Tomorrow"; 2 or 3 days left = "Soon"; all other labels remain unchanged.
- Urgent tasks go to the top of the pending tasks: overdue first, then today, then tomorrow. Done tasks stay below the pending tasks.
- NEW (client answer, 5 Nov): inside each group, the earliest due date comes first (so the most overdue task is first). A task with no due date is last in its group.
- No notification, sound or email.
- Task labels: done tasks show "Done" regardless of due date; pending tasks without a due date show "No date"; all other pending tasks use priorityLabel() with days left calculated from the due date and supplied today (never stored).
