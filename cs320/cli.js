#!/usr/bin/env node
/**
 * look-at-datman
 * Author: Alvin Lin (axl1439)
 */

const Table = require('cli-table3')
const yargs = require('yargs')

yargs.commandDir('commands')
  .demandCommand()
  .help()
  .recommendCommands()
  .strict()
  .wrap(yargs.terminalWidth())
  .parse()
