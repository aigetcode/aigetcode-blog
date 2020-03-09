import {environment} from '../../environments/environment';

const apiEndpoint: any = environment.apiEndpoint;
export enum AppConfig {
  API_ENDPOINT = apiEndpoint,

  // position map
  LAT_POSITION = 50.61074,
  LNG_POSITION = 36.58015
}
