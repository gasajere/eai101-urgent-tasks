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
| Issue / ticket | seen in the GitHub UI. |
| Branch from newest `main` | `AGENTS.md`, lesson 6: run `git fetch origin`, then branch from `origin/main`. |
| Commits on the branch | `AGENTS.md`, lesson 4: the commit message says what the commit does. |
| Push commits | `AGENTS.md`, lesson 1 points to `README.md`; `README.md`, team rule 2: run all tests before pushing. |
| PR template | `.github/pull_request_template.md`. |
| CI before merge | `.github/workflows/ci.yml`: `pull_request` trigger on PRs to `main`; job `test` runs `./gradlew cleanTest test`. |
| Red CI before merge | `.github/workflows/ci.yml`; fix the tests and push new commits to the same PR. |
| Teammate review | `AGENTS.md`, lesson 1 points to `README.md`; `README.md`, team rule 3: a teammate reviews every PR. |
| Review asks for changes | seen in the GitHub UI; the review requirement is in `README.md`, team rule 3. |
| Branch out-of-date | `AGENTS.md`, lesson 6 prevents starting from stale `main`. Whether an up-to-date branch is required is branch protection of `main` — not verified. |
| Fetch and update the branch | seen in the GitHub UI. |
| Merge conflict | seen in the GitHub UI; no repo rule file. |
| Resolve conflict locally | seen in the GitHub UI; no repo rule file. |
| Merge to `main` | `AGENTS.md`, lesson 1 points to `README.md`; `README.md`, team rule 1: never change `main` directly. Branch protection of `main` — not verified. |
| PR body has a closing keyword | `AGENTS.md`, lesson 1: put `Closes #N` in the PR body. This happens at merge, not after the post-merge CI run. |
| Issue closes automatically | `AGENTS.md`, lesson 1. |
| Issue stays open | `AGENTS.md`, lesson 1; the issue stays open until the closing keyword is added or someone closes it manually. |
| Add closing keyword and close manually | `AGENTS.md`, lesson 1; seen in the GitHub UI. |
| Merge creates a push on `main` | seen in the GitHub UI; `push` trigger is in `.github/workflows/ci.yml`. |
| CI after merge | `.github/workflows/ci.yml`: `push` trigger on `branches: [ main ]`; job `test` runs `./gradlew cleanTest test`. |
| Main is green | `.github/workflows/ci.yml`, job `test`. |
| Red CI after merge | seen in the GitHub UI; revert or open a fix branch/PR. Whether this is enforced as a merge gate is branch protection of `main` — not verified. |
