/**
 * @fileoverview Module handling the dealer subcommands.
 * @author Alvin Lin (axl1439)
 */

exports.command = 'vehicle'

exports.aliases = ['vehicles']

exports.description = 'Add, view, and manage vehicles'

exports.builder = yargs => {
  yargs.commandDir('vehicles').demandCommand()
}
