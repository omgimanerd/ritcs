/**
 * @fileoverview Add a customer to the database.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const util = require('../../lib/util')

exports.command = 'add'

exports.description = 'Add a customer'

exports.builder = yargs => {
  yargs.option('name', {
    requiresArg: true,
    required: true
  }).option('phone', {
    requiresArg: true,
    required: true
  }).option('gender', {
    requiresArg: true,
    required: true,
    choices: ['Male', 'Female', 'Other']
  }).option('income', {
    requiresArg: true,
    required: true,
    number: true
  }).option('address_street', {
    requiresArg: true,
    required: true
  }).option('address_state', {
    requiresArg: true,
    required: true
  }).option('address_zipcode', {
    requiresArg: true,
    required: true
  })
}

exports.handler = argv => {
  const client = util.getClient()
  const query = `
    insert into customers(income, name, phone, gender, address_street,
      address_state, address_zipcode) values ($1, $2, $3, $4, $5, $6, $7)`
  client.query(query,
    [
      argv.income, argv.name, argv.phone, argv.gender, argv.address_street,
      argv.address_state, argv.address_zipcode
    ]
  ).then(() => {
    console.log('Customer added.')
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
