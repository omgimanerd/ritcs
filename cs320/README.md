# CSCI 320: Principles of Data Management
Our project is a CLI built with node.js that interacts with a postgres database
to query, modify, and view data for the automotive domain. Contents:
- `cli.js` - the main entry point of the project, executed to run the CLI.
- `commands/` - contains the command submodules and source code for the CLI.
- `lib/` - contains utility modules.
- `sql/` - contains the sql code for initializing and populating the database.
- `csv/` - contains a database dump of the sample data in csv form.
- `er_diagram.pdf` - entity-relationship diagram for the project.
- `uml.pdf` - UML diagram for the project structure.

### Setup and Initialization
We recommend using `node >= 7.0.0` to run this project. To install project
dependencies:
```
cd look-at-datman/
npm install
```
To initialize the database and populate it with sample data, we have provided
three files:
  - `destroy.sql` - Deletes the tables in the database.
  - `init.sql` - Creates the necessary tables in the database.
  - `populate.sql` - Fills the tables with sample data.

These can be used to initialize and set the database into a known initial
state.

### Usage and Configuration
Before usage, the CLI must be configured with the proper database
access settings.
```
node cli.js config
  --user DATABASE_USER
  --host DATABASE_HOST
  --database DATABASE_NAME
  --password DATABASE_PASSWORD
  --port DATABASE_PORT
```
Once the database credentials and access settings are configured,
the CLI can be used. Most of the features are documented through the
`--help` flag.
```
$ node cli.js --help
cli.js <command>

Commands:
  cli.js brand     Add, view, and manage brands                               [aliases: brands]
  cli.js config    Configure the CLI settings
  cli.js customer  Add, view, and manage customers                         [aliases: customers]
  cli.js dealer    Add, view, and manage dealers                             [aliases: dealers]
  cli.js sale      View and manage sales                                       [aliases: sales]
  cli.js vehicle   Add, view, and manage vehicles                           [aliases: vehicles]

Options:
  --version  Show version number                                                      [boolean]
  --help     Show help                                                                [boolean]
```
```
$ node cli.js vehicles --help
cli.js vehicle

Add, view, and manage vehicles

Commands:
  cli.js vehicle add                   Add a vehicle
  cli.js vehicle get <vin>             Get a vehicle by its VIN number
  cli.js vehicle list                  List all vehicles
  cli.js vehicle sell <customer id>    Register the sale of one or more vehicles[aliases: buy]
  cli.js vehicle update <vin> <price>  Update the price of an unsold vehicle

Options:
  --version  Show version number                                                     [boolean]
  --help     Show help                                                               [boolean]
```

### Example usage
```
$ node cli.js brands list --country Italy
┌────────────┬─────────┬─────────────┐
│ Name       │ Country │ Reliability │
├────────────┼─────────┼─────────────┤
│ Fiat       │ Italy   │ Great       │
├────────────┼─────────┼─────────────┤
│ Maserati   │ Italy   │ Poor        │
├────────────┼─────────┼─────────────┤
│ Alfa Romeo │ Italy   │ Fair        │
├────────────┼─────────┼─────────────┤
│ Ferrari    │ Italy   │ Poor        │
└────────────┴─────────┴─────────────┘
```
```
$ node cli.js vehicles list --unowned --color Black --brand Jaguar
┌──────┬───────┬────────┬─────────────┬─────────┬────────┬───────┐
│ VIN  │ Color │ Brand  │ Model       │ Price   │ Dealer │ Owner │
├──────┼───────┼────────┼─────────────┼─────────┼────────┼───────┤
│ 1262 │ Black │ Jaguar │ E-type      │ $112763 │ EDITH  │       │
├──────┼───────┼────────┼─────────────┼─────────┼────────┼───────┤
│ 994  │ Black │ Jaguar │ XJ6 Serie 3 │ $125669 │ RONA   │       │
└──────┴───────┴────────┴─────────────┴─────────┴────────┴───────┘
```
