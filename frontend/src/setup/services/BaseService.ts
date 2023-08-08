import axios, { AxiosInstance, InternalAxiosRequestConfig } from 'axios'
import { envVariables } from '../config'

export default class BaseService {
  protected api: AxiosInstance

  constructor() {
    console.log(envVariables.apiKey)
    this.api = axios.create({
      baseURL: envVariables.apiKey,
      headers: {
        'Content-type': 'application/json',
      },
    })

    this.api.interceptors.request.use(this.handleRequest, this.handleRequestError)
  }

  private handleRequest = (config: InternalAxiosRequestConfig<unknown>): InternalAxiosRequestConfig => {
    const accessToken = localStorage.getItem('tokens')

    if (accessToken && config.headers) {
      config.headers.Authorization = `Bearer ${accessToken}`
    }

    return config
  }

  private handleRequestError = (error: unknown) => {
    return Promise.reject(error)
  }
}
