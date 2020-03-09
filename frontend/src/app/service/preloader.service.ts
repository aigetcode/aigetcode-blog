// globals.ts
import { Injectable } from '@angular/core';

@Injectable()
export class PreloaderService {
  loading: number = 1;

  public start(): number {
    this.loading = 1;
    return 1;
  }

  public stop(): number {
    this.loading = 0;
    return 0;
  }
}
