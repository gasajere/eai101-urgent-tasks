# Ticket-to-main flow

This diagram shows how a ticket becomes a commit on `main` in this repository, including the failure paths that send work back to an earlier step.

```mermaid
flowchart TD
    A([Issue / ticket]) --> B["Fetch origin and create a branch from origin/main"]
    B --> C["Make commits on the branch"]
    C --> P["Push commits to the branch"]
    P --> D["Open or update the PR using the template"]
    D --> TPR{"CI job test via pull_request to main"}

    TPR -->|"red"| F["Fix the failing tests"]
    F --> C

    TPR -->|"green"| R{"Teammate review"}

    R -->|"changes requested"| RC["Address the review comments"]
    RC --> C

    R -->|"approved"| U{"Branch up to date with main?"}

    U -->|"no"| UB["Fetch origin and update the branch"]
    UB --> MC{"Merge conflict?"}
    MC -->|"yes"| MR["Resolve the conflict locally"]
    MR --> C
    MC -->|"no"| TPR

    U -->|"yes"| M["Merge PR to main"]

    M --> CL{"PR body has a closing keyword?"}
    CL -->|"yes"| K([Issue closes automatically])
    CL -->|"no"| KO["Issue stays open"]
    KO --> KM["Add the closing keyword to the PR body and close manually"]
    KM --> K

    M --> PM["Merge creates a push on main"]
    PM --> TP{"CI job test via push to main"}
    TP -->|"green"| OK([Main is green])
    TP -->|"red"| RF["Revert or open a fix branch/PR"]
    RF --> B
```

> In `.github/workflows/ci.yml`, `pull_request` triggers the job for PRs targeting `main`, and `push` triggers it for commits pushed to `main`. A feature-branch push is tested through the `pull_request` event while the PR is open. The issue closes when the PR merges with a closing keyword in the body; the post-merge `push` test can still fail afterwards.

## Where each rule lives

| Step or failure path | Source |
| --- | --- |
| Issue / ticket | GitHub Issues UI. |
| Branch from newest `main` | `AGENTS.md`, lesson 6: run `git fetch origin`, then branch from `origin/main`. |
| Commits on the branch | `AGENTS.md`, lesson 4: the commit message says what the commit does. `README.md`, team rule 2: run all tests before pushing. |
| PR template | `.github/pull_request_template.md`. |
| CI job `test` before merge | `.github/workflows/ci.yml`: the `pull_request` trigger runs `./gradlew cleanTest test` on pull requests to `main`. |
| Red `test` | `.github/workflows/ci.yml`. Whether `test` is a required check is controlled by branch protection of `main` — not verified. |
| Teammate review | `README.md`, team rule 3: a teammate reviews every PR before it is merged. |
| Review asks for changes | GitHub review UI; the review requirement is in `README.md`, team rule 3. |
| Branch out-of-date | `AGENTS.md`, lesson 6 prevents starting from stale `main`. Whether an up-to-date branch is required is branch protection of `main` — not verified. |
| Merge conflict | Git/GitHub merge UI; there is no repo rule file for conflict resolution. |
| Merge to `main` | Branch protection of `main` — not verified. `README.md`, team rule 1 says never change `main` directly. |
| Issue closes | `AGENTS.md`, lesson 1: put `Closes #N` in the PR body. |
| Missing closing keyword | `AGENTS.md`, lesson 1; the issue stays open until the PR body is corrected or the issue is closed manually. |
| CI job `test` after merge | `.github/workflows/ci.yml`: the `push` trigger on `branches: [ main ]` runs `./gradlew cleanTest test` on the merge commit. |
| Red `test` after merge | `.github/workflows/ci.yml` runs the check; there is no repo rule file for what to do next (revert or open a fix PR). |
