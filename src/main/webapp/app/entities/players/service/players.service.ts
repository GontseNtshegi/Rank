import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlayers, getPlayersIdentifier } from '../players.model';

export type EntityResponseType = HttpResponse<IPlayers>;
export type EntityArrayResponseType = HttpResponse<IPlayers[]>;

@Injectable({ providedIn: 'root' })
export class PlayersService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/players');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(players: IPlayers): Observable<EntityResponseType> {
    return this.http.post<IPlayers>(this.resourceUrl, players, { observe: 'response' });
  }

  update(players: IPlayers): Observable<EntityResponseType> {
    return this.http.put<IPlayers>(`${this.resourceUrl}/${getPlayersIdentifier(players) as number}`, players, { observe: 'response' });
  }

  partialUpdate(players: IPlayers): Observable<EntityResponseType> {
    return this.http.patch<IPlayers>(`${this.resourceUrl}/${getPlayersIdentifier(players) as number}`, players, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlayers>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlayers[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPlayersToCollectionIfMissing(playersCollection: IPlayers[], ...playersToCheck: (IPlayers | null | undefined)[]): IPlayers[] {
    const players: IPlayers[] = playersToCheck.filter(isPresent);
    if (players.length > 0) {
      const playersCollectionIdentifiers = playersCollection.map(playersItem => getPlayersIdentifier(playersItem)!);
      const playersToAdd = players.filter(playersItem => {
        const playersIdentifier = getPlayersIdentifier(playersItem);
        if (playersIdentifier == null || playersCollectionIdentifiers.includes(playersIdentifier)) {
          return false;
        }
        playersCollectionIdentifiers.push(playersIdentifier);
        return true;
      });
      return [...playersToAdd, ...playersCollection];
    }
    return playersCollection;
  }
}
