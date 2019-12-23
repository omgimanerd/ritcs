/**
 * @fileoverview Module handling the dealer subcommands.
 * @author Alvin Lin (axl1439)
 */

exports.command = 'sale'

exports.aliases = ['sales']

exports.description = 'View and manage sales'

exports.builder = yargs => {
  yargs.commandDir('sales').demandCommand()
}
