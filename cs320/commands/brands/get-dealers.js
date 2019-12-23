/**
 * @fileoverview Get the dealers that sell a brand.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const display = require('../../lib/display')
const util = require('../../lib/util')

exports.command = 'get-dealers <brand name>'

exports.description = 'Get the dealers that sell a brand'

exports.handler = argv => {
  const client = util.getClient()
  const matchingDealerIds = `
    select id from dealers where id in
      (select dealer from brand_dealer where brand = $1)`
  const subquery = `
    select sale, d.id, d.name, d.phone, sum(price) as price from
      vehicles v inner join dealers d on v.dealer = d.id
      where sale is not null and d.id in (${matchingDealerIds})
      group by d.id,sale`
  const query = `
    select id, name, phone, avg(price) as price from
      (${subquery}) as sub
      group by id, name, phone order by id`
  client.query(query, [argv.brandname]).then(result => {
    display.displayDealers(result.rows)
    client.end()
  }).catch(error => {
    console.error(error)
    client.end()
  })
}
