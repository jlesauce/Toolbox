# Toolbox

## How to install library to Maven local repository

```console
mvn install
```

## How to publish a new release

```console
./update_version.py
git commit -am "Package release v<version-number>"
git push
git tag -a v<version-number> -m "Package release v<version-number>"
mvn clean deploy
```
