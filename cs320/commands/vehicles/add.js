/**
 * @fileoverview Add a vehicle to the database.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const util = require('../../lib/util')

exports.command = 'add'

exports.description = 'Add a vehicle'

exports.builder = yargs => {
  yargs.option('vin', {
    requiresArg: true,
    required: true,
    number: true
  }).option('model', {
    requiresArg: true,
    required: true
  }).option('transmission', {
    requiresArg: true,
    required: true
  }).option('mileage', {
    requiresArg: true,
    required: true,
    number: true
  }).option('engine', {
    requiresArg: true,
    required: true
  }).option('color', {
    requiresArg: true,
    required: true
  }).option('price', {
    requiresArg: true,
    required: true,
    number: true
  }).option('brand', {
    requiresArg: true,
    required: true
  }).option('dealer', {
    requiresArg: true,
    required: true,
    number: true
  })
}

exports.handler = argv => {
  const client = util.getClient()
  const query = `
    insert into vehicles(vin, model, transmission,
      mileage, engine, color, price, brand, dealer)
      values ($1, $2, $3, $4, $5, $6, $7, $8, $9)`
  client.query(query,
    [
      argv.vin, argv.model, argv.transmission, argv.mileage, argv.engine,
      argv.color, argv.price, argv.brand, argv.dealer
    ]
  ).then(() => {
    console.log('Vehicle added.')
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
