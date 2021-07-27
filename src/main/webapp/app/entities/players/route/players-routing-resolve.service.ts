import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlayers, Players } from '../players.model';
import { PlayersService } from '../service/players.service';

@Injectable({ providedIn: 'root' })
export class PlayersRoutingResolveService implements Resolve<IPlayers> {
  constructor(protected service: PlayersService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlayers> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((players: HttpResponse<Players>) => {
          if (players.body) {
            return of(players.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Players());
  }
}
