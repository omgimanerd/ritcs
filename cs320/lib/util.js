/**
 * @fileoverview Utility module.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const pg = require('pg')
const _ = require('underscore')

const config = require('./config')

const getClient = () => {
  const connectionObject = config.getConfig()
  const client = new pg.Client(connectionObject)
  client.connect()
  return client
}

const sellVehicle = (customerId, vins) => {
  const numVins = vins.length
  const vinParameters = _.range(numVins).map(i => `$${i + 1}`).join(', ')
  if (numVins === 0) {
    console.log('No vin numbers provided.')
    console.log('Run: node cli.js vehicle sell --help')
    return
  }
  const client = getClient()
  const verificationQuery = `
    select dealer, owner from vehicles where vin in (${vinParameters})`
  client.query(verificationQuery, vins).then(verificationResult => {
    const rows = verificationResult.rows
    const dealers = _.unique(rows.map(row => row.dealer))
    if (dealers.length !== 1) {
      console.log('These vehicles belong to different dealers.')
      client.end()
      return
    }
    const owners = _.unique(rows.map(row => row.owner))
    if (!_.every(owners, _.isNull)) {
      console.log('One of the specified vehicles is already owned.')
      client.end()
      return
    }
    const sellQuery = `
      with sale as (insert into sales(close_date, customer, dealer) values(
        current_date, $${numVins + 1}, $${numVins + 2})
        returning id)
      update vehicles set
        owner = $${numVins + 1}, sale = (select id from sale)
        where vin in (${vinParameters})`
    client.query(
      sellQuery, vins.concat([customerId, dealers[0]])
    ).then(result => {
      if (result.rowCount === numVins) {
        console.log('Vehicle sale recorded.')
      } else {
        console.log('There was an error recording this purchase.')
      }
      client.end()
    }).catch(error => {
      console.error(error)
      client.end()
    })
  })
}

module.exports = exports = { getClient, sellVehicle }
