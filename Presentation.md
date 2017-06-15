- Test 1 & 4: include menu titles
  * Design changes
  * I10n

- Test 8:

    * Includes UUID to create truely unique id

- Refactored test 8 to make

  * createNewUniqueWhipBird
      - Should this include or exclude logIn() method?

  * deleteFirstWhipBird

  * deleteAllWhipBirds

- Test 9

  - weird inconsistencies with success failures
    - Success rate c. 20%
    - with wait time > 10 seconds (line 156) improved to c. 40%
    - Wait time is 500ms. Need to make this less than angular reload speed to achieve better results.
  - Belt and braces:
    - Check "no-whipbirds-saved" id and text not present - use of UUID is crucial.


- Commit messages should be more descriptive
