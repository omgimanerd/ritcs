/**
 * @fileoverview This module handles displaying various database resources in
 *   neat and orderly tables.
 * @author Alvin Lin (axl1439)
 */

// eslint-disable-next-line no-unused-vars
const colors = require('colors')
const Table = require('cli-table3')

const format = require('./format')

const pushNone = table => {
  table.push([{
    colSpan: table.options.head.length,
    content: 'none',
    hAlign: 'center'
  }])
}

exports.displayBrands = brands => {
  const table = new Table({ head: ['Name', 'Country', 'Reliability'] })
  if (brands.length === 0) {
    pushNone(table)
  } else {
    table.push(...brands.map(brand => [
      String(brand.name).green.bold, brand.country,
      format.formatReliability(brand.reliability)
    ]))
  }
  console.log(table.toString())
}

exports.displayCustomer = customer => {
  const table = new Table()
  const street = customer.address_street
  const state = customer.address_state
  const zip = customer.address_zipcode
  table.push(...format.formatLeftColumnHeader([
    ['ID', String(customer.id).cyan.bold],
    ['Name', customer.name],
    ['Gender', customer.gender],
    ['Phone', customer.phone],
    ['Income', customer.income],
    ['Address', `${street}, ${state} ${zip}`]
  ]))
  console.log(table.toString())
}

exports.displayCustomers = customers => {
  const table = new Table({
    head: ['ID', 'Name', 'Phone', 'Gender', 'State', 'ZIP']
  })
  if (customers.length === 0) {
    pushNone(table)
  } else {
    table.push(...customers.map(customer => [
      String(customer.id).cyan.bold, customer.name, customer.phone,
      customer.gender, customer.address_state, customer.address_zipcode
    ]))
  }
  console.log(table.toString())
}

exports.displayDealers = dealers => {
  const table = new Table({
    head: ['ID', 'Name', 'Phone', 'Average Sale']
  })
  if (dealers.length === 0) {
    pushNone(table)
  } else {
    table.push(...dealers.map(dealer => [
      String(dealer.id).cyan.bold, dealer.name, dealer.phone,
      `$${parseFloat(dealer.price, 2).toFixed(2)}`.green
    ]))
  }
  console.log(table.toString())
}

exports.displaySale = sale => {
  const table = new Table()
  table.push(...format.formatLeftColumnHeader([
    ['ID', String(sale.id).cyan.bold],
    ['Dealer', sale.dealer],
    ['Customer', sale.customer],
    ['Amount', `$${sale.price}`.green]
  ]))
  console.log(table.toString())
}

exports.displaySales = sales => {
  const table = new Table({
    head: ['ID', 'Dealer', 'Customer', 'Amount', 'Date']
  })
  if (sales.length === 0) {
    pushNone(table)
  } else {
    table.push(...sales.map(sale => [
      String(sale.id).cyan.bold, sale.dealer, sale.customer,
      `$${sale.price}`.green, sale.close_date.toDateString()
    ]))
  }
  console.log(table.toString())
}

exports.displayVehicle = vehicle => {
  const table = new Table()
  table.push(...format.formatLeftColumnHeader([
    ['VIN', String(vehicle.vin).cyan.bold],
    ['Color', vehicle.color],
    ['Brand', vehicle.brand],
    ['Model', vehicle.model],
    ['Transmission', vehicle.transmission],
    ['Engine', vehicle.engine],
    ['Mileage', vehicle.mileage],
    ['Price', `$${vehicle.price}`.green],
    ['Dealer', vehicle.dealer],
    ['Owner', vehicle.owner],
    ['Sale ID', vehicle.sale]
  ]))
  console.log(table.toString())
}

exports.displayVehicles = vehicles => {
  const table = new Table({ head: [
    'VIN', 'Color', 'Brand', 'Model', 'Price', 'Dealer', 'Owner'
  ] })
  if (vehicles.length === 0) {
    pushNone(table)
  } else {
    table.push(...vehicles.map(vehicle => [
      String(vehicle.vin).cyan.bold, vehicle.color, vehicle.brand,
      vehicle.model, `$${vehicle.price}`.green, vehicle.dealer, vehicle.owner
    ]))
  }
  console.log(table.toString())
}
