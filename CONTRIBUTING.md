# Contributing to the Keycloak EDS Plugin

The Keycloak EDS Plugin is open source.

Contributions to this project are warmly welcomed, and there are numerous 
ways you can do so. We welcome everything from creating informative 
tutorials or intriguing blog posts, enhancing our existing documentation, 
submitting reports for bugs and proposing new features, to writing actual code.

## Code contributions

If you would like to contribute a bug fix or new feature to the project, please follow the steps below:

- Firstly, review the open issues and pull requests to ensure there are no similar issues or proposed changes already present.
- Begin by opening an issue, taking care to thoroughly describe the context and any potential solutions. Remember to link related issues or pull requests, if applicable.
- Proceed to open a pull request and correlate your code amendments with the issue you created in the previous step.

By doing so, you can:
- Facilitate knowledge sharing and documentation of a bug or absent feature.
- Obtain feedback and avoid duplicative work if someone else is already addressing the issue.
- Leverage the team's collective experience through discussion. This is invaluable as several implementation details may not be immediately apparent.

### Testing

The test suite consists of unit tests.
To run the unit tests, simply execute

```bash
mvn clean test
```

### Configuring IDEs

#### IntelliJ

- Import the repository as a maven project

### Java Language Formatting Guidelines

Please follow these formatting guidelines:

* Java indent is 4 spaces
* Line width is 140 characters
* The rest is left to Java coding standards
* Disable “auto-format on save” to prevent unnecessary format changes.
  This makes reviews much harder as it generates unnecessary formatting changes.
  If your IDE supports formatting only modified chunks that is fine to do.
* Wildcard imports (`import foo.bar.baz.*`) are forbidden.
  Please attempt to tame your IDE so it doesn't make them and please send a PR against this document with instructions for your IDE if it doesn't contain them.
* Eclipse: `Preferences->Java->Code Style->Organize Imports`.
  There are two boxes labeled "`Number of (static )? imports needed for .*`".
  Set their values to 99999 or some other absurdly high value.
* IntelliJ: `Preferences/Settings->Editor->Code Style->Java->Imports`.
  There are two configuration options: `Class count to use import with '*'` and `Names count to use static import with '*'`.
  Set their values to 99999 or some other absurdly high value.
* Don't worry too much about import order.
  Try not to change it but don't worry about fighting your IDE to stop it from doing so.

### License Headers

We require license headers on all Java files.