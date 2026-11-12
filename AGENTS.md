## Team rules

rules for any AI that works in this repo
1. The current client rules (README, "Client rules") are the only source of truth.
   If a rule is missing or two sources disagree, stop and ask.
2. Store the due date; calculate urgency using the supplied date.
3. Urgency and sorting functions use the supplied date; tests use fixed dates.
4. Do only what the ticket asks: no rename, no refactor, no extra feature
   (no notification, sound or email).

## Lessons, week of 9 Nov

1. Put "Closes #N" in the PR body, not only in the title, so the issue is linked and closed on merge. — [PR #20](https://github.com/gasajere/eai101-urgent-tasks/pull/20)
2. Show each new test failing once, and make sure the failure line names that test. — [PR #21](https://github.com/gasajere/eai101-urgent-tasks/pull/21)
3. Never change a test's expected value to make it pass; ask which rule is current, and let README.md decide. — [PR #6](https://github.com/gasajere/eai101-urgent-tasks/pull/6)
4. Write a commit message that says what the commit does, not a result ("test failed") or a flag ("--no-edit"). — [commit 7a33543](https://github.com/gasajere/eai101-urgent-tasks/commit/7a33543a8f35ae8e6718e2d68ef1441c1eb37084)
5. Make "Not checked" list the real risks of this change; if you know none, write "Nothing known". — [PR #7](https://github.com/gasajere/eai101-urgent-tasks/pull/7)
6. Start each branch from the newest main: run `git fetch origin`, then branch from `origin/main`. — [commit fbcc4b1](https://github.com/gasajere/eai101-urgent-tasks/commit/fbcc4b10e62aabf95bba0cfd93a0ac9814a53fad)
7. Stop and ask a person before a push to main, a change in `.github/workflows/`, a change to branch protection, reading a secret or token, or adding a dependency. — [PR #22](https://github.com/gasajere/eai101-urgent-tasks/pull/22)