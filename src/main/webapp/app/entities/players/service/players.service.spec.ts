import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPlayers, Players } from '../players.model';

import { PlayersService } from './players.service';

describe('Service Tests', () => {
  describe('Players Service', () => {
    let service: PlayersService;
    let httpMock: HttpTestingController;
    let elemDefault: IPlayers;
    let expectedResult: IPlayers | IPlayers[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PlayersService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        currentBalance: 0,
        wageringMoney: 0,
        winningMoney: 0,
        username: 'AAAAAAA',
        email: 'AAAAAAA',
        contact: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Players', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Players()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Players', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            currentBalance: 1,
            wageringMoney: 1,
            winningMoney: 1,
            username: 'BBBBBB',
            email: 'BBBBBB',
            contact: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Players', () => {
        const patchObject = Object.assign(
          {
            wageringMoney: 1,
            email: 'BBBBBB',
          },
          new Players()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Players', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            currentBalance: 1,
            wageringMoney: 1,
            winningMoney: 1,
            username: 'BBBBBB',
            email: 'BBBBBB',
            contact: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Players', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPlayersToCollectionIfMissing', () => {
        it('should add a Players to an empty array', () => {
          const players: IPlayers = { id: 123 };
          expectedResult = service.addPlayersToCollectionIfMissing([], players);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(players);
        });

        it('should not add a Players to an array that contains it', () => {
          const players: IPlayers = { id: 123 };
          const playersCollection: IPlayers[] = [
            {
              ...players,
            },
            { id: 456 },
          ];
          expectedResult = service.addPlayersToCollectionIfMissing(playersCollection, players);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Players to an array that doesn't contain it", () => {
          const players: IPlayers = { id: 123 };
          const playersCollection: IPlayers[] = [{ id: 456 }];
          expectedResult = service.addPlayersToCollectionIfMissing(playersCollection, players);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(players);
        });

        it('should add only unique Players to an array', () => {
          const playersArray: IPlayers[] = [{ id: 123 }, { id: 456 }, { id: 40413 }];
          const playersCollection: IPlayers[] = [{ id: 123 }];
          expectedResult = service.addPlayersToCollectionIfMissing(playersCollection, ...playersArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const players: IPlayers = { id: 123 };
          const players2: IPlayers = { id: 456 };
          expectedResult = service.addPlayersToCollectionIfMissing([], players, players2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(players);
          expect(expectedResult).toContain(players2);
        });

        it('should accept null and undefined values', () => {
          const players: IPlayers = { id: 123 };
          expectedResult = service.addPlayersToCollectionIfMissing([], null, players, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(players);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
