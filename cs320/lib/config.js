/**
 * @fileoverview Config setter and getter.
 * @author alvin@omgimanerd.tech (Alvin Lin)
 */

const _ = require('underscore')

const fs = require('fs')
const path = require('path')
const os = require('os')

const CONFIG_PATH = path.join(os.homedir(), '.look-at-datman-config.json')

const getConfig = () => {
  try {
    // eslint-disable-next-line no-sync
    return JSON.parse(fs.readFileSync(CONFIG_PATH, 'utf-8'))
  } catch (error) {
    console.log('Unable to read config file, using default settings.'.red)
    return {}
  }
}

const updateConfig = config => {
  try {
    const newConfig = _.mapObject(_.extend(getConfig(), config), v => {
      return String(v).toLowerCase() === 'null' ? null : v
    })
    // eslint-disable-next-line no-sync
    fs.writeFileSync(CONFIG_PATH, JSON.stringify(newConfig), { mode: 0o600 })
    return { error: null }
  } catch (error) {
    return { error }
  }
}

module.exports = exports = { getConfig, updateConfig }
