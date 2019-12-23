/**
 * @fileoverview Module handling the dealer subcommands.
 * @author Alvin Lin (axl1439)
 */

exports.command = 'dealer'

exports.aliases = ['dealers']

exports.description = 'Add, view, and manage dealers'

exports.builder = yargs => {
  yargs.commandDir('dealers').demandCommand()
}
