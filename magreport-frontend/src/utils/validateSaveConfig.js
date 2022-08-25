/**
 * Проверка на валидацию сохраненных полей конфигурации olapConfig с полями из Metadata
 * @param {object} responseConfigData - объект c значениями fieldsLists из сохраненной конфигурации
 * @param {array} allFields - объект c полями, загруженных из Metadata
 */

export default function validateSaveConfig(responseConfigData, allFields) {
	let configsIdsArr = []
	
	for (var key in responseConfigData) {

		if(responseConfigData[key].length !== 0 && Array.isArray(responseConfigData[key])) {
			responseConfigData[key].map( item => configsIdsArr.push(item.id) )
		}
	}

	const resultArray = configsIdsArr.map( (id) => {
		return allFields.some( (fieldData) => {
			return fieldData.id === id
		})
	})

	return resultArray.includes(false) // если ответ false значит в responseConfigData нет отличающих значений с allFields
}