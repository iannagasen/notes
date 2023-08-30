## Submodules:
  - a project within a project
  - a github repo within a repo

## Why/When
  - merge repo for backend and frontend

Command:
`git submodule add <github-repo>`

- it will create a `.gitmodules`

```.gitmodules
[submodule "youtube-tutorials"]
  path = youtube-tutorials
  url = https://github.com/....
```
