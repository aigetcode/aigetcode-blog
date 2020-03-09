import {Component} from '@angular/core';
import {ChartDataSets, ChartOptions, ChartType} from 'chart.js';
import {Label} from 'ng2-charts';
import * as pluginDataLabels from 'chartjs-plugin-datalabels';
import {AdminService} from '../../../service/admin.service';
import {StatisticDto} from '../../../models/statistic.dto';
import {AuthenticationService} from '../../../service/authentication.service';
import {AuthModel} from '../../../models/auth.model';

@Component({
  selector: 'statistic',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistic.css'],
})
export class StatisticsComponent {
  public barChartPlugins = [pluginDataLabels];
  public barChartType: ChartType = 'bar';
  public barChartLegend = true;
  public barChartLabels: Label[] = [];
  public statistic: StatisticDto;
  public monthNames = ['January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ];
  public barChartData: ChartDataSets[] = [];

  public barChartOptions: ChartOptions = {
    responsive: true,
    scales: {
      xAxes: [{}],
      yAxes: [{}] },
    plugins: {
      datalabels: {
        anchor: 'end',
        align: 'end',
      }
    }
  };

  public colors = [
    { backgroundColor: '#1ca5a0' },
    { backgroundColor: '#616de4' },
  ];

  constructor(private adminService: AdminService,
              private authenticationService: AuthenticationService) {

    const data: AuthModel = this.authenticationService.currentTokenData;
    this.adminService.getStatisticData(data.token).subscribe(result => {
      this.statistic = result;
      let dateMonth: number = (new Date()).getMonth();
      const firstKeyObject: string = Object.keys(this.statistic.statisticByMonth)[0];
      const sizeArray: number[] = this.statistic.statisticByMonth[firstKeyObject];

      for (let i = 0; i < sizeArray.length; i++) {
        if (dateMonth < 0) {
          dateMonth = 11;
        }
        const month = this.monthNames[Math.abs(dateMonth)];
        this.barChartLabels.unshift(month);
        dateMonth--;
      }

      for (const key of Object.keys(this.statistic.statisticByMonth)) {
        const statObj = {
          label: key,
          data: this.statistic.statisticByMonth[key],
        };
        this.barChartData.push(statObj);
      }
    });
  }
}
