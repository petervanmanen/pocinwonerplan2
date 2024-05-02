import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './inwonerplan-activiteit.reducer';

export const InwonerplanActiviteitDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const inwonerplanActiviteitEntity = useAppSelector(state => state.inwonerplanActiviteit.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="inwonerplanActiviteitDetailsHeading">Inwonerplan Activiteit</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{inwonerplanActiviteitEntity.id}</dd>
          <dt>
            <span id="actiehouder">Actiehouder</span>
          </dt>
          <dd>{inwonerplanActiviteitEntity.actiehouder}</dd>
          <dt>
            <span id="begindatum">Begindatum</span>
          </dt>
          <dd>
            {inwonerplanActiviteitEntity.begindatum ? (
              <TextFormat value={inwonerplanActiviteitEntity.begindatum} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="einddatum">Einddatum</span>
          </dt>
          <dd>
            {inwonerplanActiviteitEntity.einddatum ? (
              <TextFormat value={inwonerplanActiviteitEntity.einddatum} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="naam">Naam</span>
          </dt>
          <dd>{inwonerplanActiviteitEntity.naam}</dd>
          <dt>
            <span id="status">Status</span>
          </dt>
          <dd>{inwonerplanActiviteitEntity.status}</dd>
          <dt>Inwonerplan Sub Doel</dt>
          <dd>{inwonerplanActiviteitEntity.inwonerplanSubDoel ? inwonerplanActiviteitEntity.inwonerplanSubDoel.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/inwonerplan-activiteit" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/inwonerplan-activiteit/${inwonerplanActiviteitEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default InwonerplanActiviteitDetail;
