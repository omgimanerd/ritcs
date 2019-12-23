/**
 * @fileoverview Set config for CLI.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const _ = require('underscore')

const config = require('../lib/config')

exports.command = 'config'

exports.description = 'Configure the CLI settings'

exports.builder = yargs => {
  yargs.option('user', {
    description: 'Set database user',
    requiresArg: true
  }).option('host', {
    description: 'Set database hostname',
    requiresArg: true
  }).option('database', {
    description: 'Set database name',
    requiresArg: true
  }).option('password', {
    description: 'Set database password',
    requiresArg: true
  }).option('port', {
    description: 'Set database port',
    requiresArg: true,
    number: true
  })
}

exports.handler = argv => {
  const configUpdate = _.omit(argv, ['_', '$0'])
  if (!_.isEmpty(configUpdate)) {
    const status = config.updateConfig(configUpdate)
    if (status.error) {
      console.error('An error occurred when writing the config.')
    }
  } else {
    console.error('No keys specified for update.')
    console.error('Run: node cli.js config --help')
  }
}
