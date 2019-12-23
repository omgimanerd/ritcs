/**
 * @fileoverview Search for customers.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const _ = require('underscore')

const display = require('../../lib/display')
const util = require('../../lib/util')

exports.command = 'list'

exports.description = 'List customers with an optional filter'

exports.builder = yargs => {
  yargs.option('name', {
    description: 'Limit results by customer name',
    requiresArg: true
  }).option('phone', {
    description: 'Limit results by phone number',
    requiresArg: true
  }).option('gender', {
    description: 'Limit results by gender',
    requiresArg: true,
    choices: ['Male', 'Female', 'Other']
  }).option('address_state', {
    description: 'Limit results by state',
    requiresArg: true
  }).option('address_zip', {
    description: 'Limit results by zipcode',
    requiresArg: true
  })
}

exports.handler = argv => {
  const client = util.getClient()
  const conditions = []
  const substitutions = []
  _.keys(_.omit(argv, ['_', '$0'])).forEach((field, i) => {
    if (field === 'name') {
      conditions.push(`name like $${i + 1}`)
      substitutions.push(`${argv[field]}%`)
    } else {
      conditions.push(`${field} = $${i + 1}`)
      substitutions.push(argv[field])
    }
  })
  const whereClause = conditions.length === 0 ?
    '' : `where ${conditions.join(' and ')}`
  const query = `select * from customers ${whereClause} order by name`
  client.query(query, substitutions).then(result => {
    display.displayCustomers(result.rows)
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
