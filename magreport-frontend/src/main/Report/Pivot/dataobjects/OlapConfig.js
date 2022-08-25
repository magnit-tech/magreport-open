import dataHub from "ajax/DataHub"

/**
 * Объект OLAP-конфигурации - содержит все данные для дальнейшего сохранения, загрузки конфигураци.
 */
export default function OlapConfig(config){
    
    /*
        **************
        Поля
        **************
    */

    this.configData = {
        'isCurrent': false,
        'isDefault': false,
        'isShare': false,
        'jobId': null,
        "olapConfig": {
            "olapConfigData": {},
            "olapConfigDescription": '',
            "olapConfigId": null,
            "olapConfigName": ''
        },
        "reportId": null,
        'reportOlapConfigId': null,
        "userId": null
    }  

    /*
        **************
        Методы
        **************
    */

    // Преобразуем поля configData на основе полученного объекта текущей конфигурации
    this.createLists = (config) => {
        this.configData = {
            'isCurrent': config?.isCurrent || false,
            'isDefault': config?.isDefault || false,
            'isShare': config?.isShare || false,
            'jobId': config?.jobId || null,
            "olapConfig": {
                "olapConfigData": config?.olapConfig?.data || {},
                "olapConfigDescription": config?.olapConfig?.description || '',
                "olapConfigId": config?.olapConfig?.id || null,
                "olapConfigName": config?.olapConfig?.name || ''
            },
            "reportId": config?.report || null,
            'reportOlapConfigId': config?.reportOlapConfigId || null,
            "userId": config?.user?.id || null
        }
    }  

	// Cохранение текущей конфигурации
    this.saveCurrentConfig = (pivotConfigForSave) => {

        const { olapConfigDescription, olapConfigId, olapConfigName } = this.configData.olapConfig

        let payloadData = {
			...this.configData,
            "isCurrent": true,
            "olapConfig": {
				"olapConfigData": pivotConfigForSave,
				"olapConfigDescription": olapConfigDescription,
				"olapConfigId": olapConfigId,
				"olapConfigName": olapConfigName
			}
        }

        dataHub.olapController.saveConfig(payloadData, ({ok, data}) => ok && this.createLists(data) )
    }

    // Cохранение новой конфигурации (не текущей)
    this.saveNewConfig = (obj, reportId, callback) => {

        const { name, description, saveFor } = obj
        
        let payloadData = {
			...this.configData,
			'isCurrent': false,
			'jobId': saveFor === 'job' ? this.configData.jobId : null,
			"olapConfig": {
			  "olapConfigData": this.configData.olapConfig.olapConfigData,
			  "olapConfigDescription": description,
			  "olapConfigId": null,
			  "olapConfigName": name
			},
			'reportId': saveFor === 'report' ? reportId : null,
			'reportOlapConfigId': null,
		}

        dataHub.olapController.saveConfig(payloadData, ({ok}) => callback(ok, name) )
    }

	// Cохранение выбранной конфигурации (не текущей)
	this.saveExistingConfig = (obj, reportId, callback) => {

        const { name, description, saveFor, olapConfigId = null, reportOlapConfigId = null } = obj
        
        let payloadData = {
			...this.configData,
			'isCurrent': false,
			'jobId': saveFor === 'job' ? this.configData.jobId : null,
			"olapConfig": {
			  "olapConfigData": this.configData.olapConfig.olapConfigData,
			  "olapConfigDescription": description,
			  "olapConfigId": olapConfigId,
			  "olapConfigName": name
			},
			'reportId': saveFor === 'report' ? reportId : null,
			reportOlapConfigId
		}

		dataHub.olapController.saveConfig(payloadData, ({ok}) => callback(ok, name) )
    }

	// Загрузка выбранной конфигурации
    this.loadChosenConfig = (obj, callback) => {
        
        const { olapConfigDescription, olapConfigId, olapConfigName } = this.configData.olapConfig

        let payloadData = {
			...this.configData,
            "isCurrent": true,
            "olapConfig": {
				"olapConfigData": obj,
				"olapConfigDescription": olapConfigDescription,
				"olapConfigId": olapConfigId,
				"olapConfigName": olapConfigName
			}
        }

		dataHub.olapController.saveConfig(payloadData, ({ok, data}) => {
            if (ok) {
                this.createLists(data)
                return callback(ok)
            }

            return callback(ok)
        })


    }

    // Удаление конфигурации
    this.deleteConfig = (reportOlapConfigId, callback) => {

        dataHub.olapController.deleteConfig(reportOlapConfigId, ({ok}) => callback(ok))

    }
}