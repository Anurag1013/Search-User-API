# Coding guidelines included in this repo

- SOLID principles followed (interfaces for services, single responsibility classes)
- DRY: mapping and utility code centralized in service/mappers
- WET avoided: helper methods used for repeated tasks
- Logging: use slf4j (log.info/debug/error) consistently
- Exception handling: centralized in GlobalExceptionHandler
- Profiles: local, dev, test, prod in application.yml
