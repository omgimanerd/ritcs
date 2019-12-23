/**
 * @fileoverview Module handling the customer subcommands.
 * @author Alvin Lin (axl1439)
 */

exports.command = 'customer'

exports.aliases = ['customers']

exports.description = 'Add, view, and manage customers'

exports.builder = yargs => {
  yargs.commandDir('customers').demandCommand()
}
