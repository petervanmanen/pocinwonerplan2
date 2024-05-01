import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAandachtspunt } from 'app/shared/model/aandachtspunt.model';
import { getEntities as getAandachtspunts } from 'app/entities/aandachtspunt/aandachtspunt.reducer';
import { IOntwikkelwens } from 'app/shared/model/ontwikkelwens.model';
import { getEntities as getOntwikkelwens } from 'app/entities/ontwikkelwens/ontwikkelwens.reducer';
import { ISubdoel } from 'app/shared/model/subdoel.model';
import { getEntity, updateEntity, createEntity, reset } from './subdoel.reducer';

export const SubdoelUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const aandachtspunts = useAppSelector(state => state.aandachtspunt.entities);
  const ontwikkelwens = useAppSelector(state => state.ontwikkelwens.entities);
  const subdoelEntity = useAppSelector(state => state.subdoel.entity);
  const loading = useAppSelector(state => state.subdoel.loading);
  const updating = useAppSelector(state => state.subdoel.updating);
  const updateSuccess = useAppSelector(state => state.subdoel.updateSuccess);

  const handleClose = () => {
    navigate('/subdoel');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAandachtspunts({}));
    dispatch(getOntwikkelwens({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.code !== undefined && typeof values.code !== 'number') {
      values.code = Number(values.code);
    }

    const entity = {
      ...subdoelEntity,
      ...values,
      aandachtspunt: aandachtspunts.find(it => it.id.toString() === values.aandachtspunt?.toString()),
      ontwikkelwens: ontwikkelwens.find(it => it.id.toString() === values.ontwikkelwens?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...subdoelEntity,
          aandachtspunt: subdoelEntity?.aandachtspunt?.id,
          ontwikkelwens: subdoelEntity?.ontwikkelwens?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="demo2App.subdoel.home.createOrEditLabel" data-cy="SubdoelCreateUpdateHeading">
            Create or edit a Subdoel
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="subdoel-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Code" id="subdoel-code" name="code" data-cy="code" type="text" />
              <ValidatedField label="Naam" id="subdoel-naam" name="naam" data-cy="naam" type="text" />
              <ValidatedField label="Actief" id="subdoel-actief" name="actief" data-cy="actief" check type="checkbox" />
              <ValidatedField id="subdoel-aandachtspunt" name="aandachtspunt" data-cy="aandachtspunt" label="Aandachtspunt" type="select">
                <option value="" key="0" />
                {aandachtspunts
                  ? aandachtspunts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="subdoel-ontwikkelwens" name="ontwikkelwens" data-cy="ontwikkelwens" label="Ontwikkelwens" type="select">
                <option value="" key="0" />
                {ontwikkelwens
                  ? ontwikkelwens.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/subdoel" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default SubdoelUpdate;
