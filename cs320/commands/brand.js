/**
 * @fileoverview Module handling the dealer subcommands.
 * @author Alvin Lin (axl1439)
 */

exports.command = 'brand'

exports.aliases = ['brands']

exports.description = 'Add, view, and manage brands'

exports.builder = yargs => {
  yargs.commandDir('brands').demandCommand()
}
