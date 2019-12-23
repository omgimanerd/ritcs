/**
 * @fileoverview Update a dealer in the database.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const _ = require('underscore')

const util = require('../../lib/util')

exports.command = 'update <dealer id>'

exports.description = 'Update a dealer\'s information'

exports.builder = yargs => {
  yargs.option('name', {
    requiresArg: true,
    string: true
  }).option('phone', {
    requiresArg: true,
    number: true
  })
}

exports.handler = argv => {
  const id = argv.dealerid
  argv = _.pick(argv, ['name', 'phone'])
  if (_.isEmpty(argv)) {
    console.log('No update parameters were provided.')
    console.log('Run: node cli.js dealer update --help')
    return
  }
  const changes = []
  const substitutions = []
  _.keys(argv).forEach((field, i) => {
    changes.push(`${field} = $${i + 2}`)
    substitutions.push(`${argv[field]}`)
  })
  const query = `update dealers set ${changes.join(', ')} where id = $1`
  const client = util.getClient()
  client.query(query, [id].concat(substitutions)).then(result => {
    if (result.rowCount === 1) {
      console.log('Dealer updated.')
    } else {
      console.log('No such dealer exists.')
    }
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
