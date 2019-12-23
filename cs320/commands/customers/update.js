/**
 * @fileoverview Update a customer's information.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const _ = require('underscore')

const util = require('../../lib/util')

exports.command = 'update <customer id>'

exports.description = 'Update a customer\'s information'

exports.builder = yargs => {
  yargs.option('name', {
    requiresArg: true,
    string: true
  }).option('phone', {
    requiresArg: true,
    number: true
  }).option('gender', {
    requiresArg: true,
    choices: ['Male', 'Female', 'Other']
  }).option('income', {
    requiresArg: true,
    number: true
  }).option('address_street', {
    requiresArg: true
  }).option('address_state', {
    requiresArg: true
  }).option('address_zipcode', {
    requiresArg: true,
    number: true
  })
}

exports.handler = argv => {
  const id = argv.customerid
  argv = _.omit(argv, ['_', '$0', 'customerid'])
  if (_.isEmpty(argv)) {
    console.log('No update parameters were provided.')
    console.log('Run: node cli.js customers update --help')
    return
  }
  const changes = []
  const substitutions = []
  _.keys(argv).forEach((field, i) => {
    changes.push(`${field} = $${i + 2}`)
    substitutions.push(`${argv[field]}`)
  })
  const query = `update customers set ${changes.join(', ')} where id = $1`
  const client = util.getClient()
  client.query(query, [id].concat(substitutions)).then(result => {
    if (result.rowCount === 1) {
      console.log('Customer updated.')
    } else {
      console.log('No such customer exists.')
    }
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
